package server;

import com.google.gson.Gson;
import dependencies.fileprocessing.TransmissionObject;
import dependencies.fileprocessing.TransmissionObjectBuilder;
import dependencies.fileprocessing.TransmissionObjectType;
import dependencies.fileprocessing.gpx.GpxFile;
import dependencies.fileprocessing.gpx.GpxResults;
import dependencies.fileprocessing.gpx.WaypointImpl;
import dependencies.mapper.Map;
import dependencies.user.GenericStats;
import dependencies.user.Route;
import dependencies.user.Segment;
import dependencies.user.UserData;
import fileprocessing.ClientData;
import user.Authentication;
import user.userdata.DataExchangeHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import static server.Utils.*;
import static user.userdata.DataExchangeHandler.populateGenericStatsObject;


/**
 * This class is responsible to handle the connection between one client and the server. It gets instantiated from the
 * ClientListener every time a new client connects. The new object is put in a ArrayList for later management.
 * e.g. Access to the ClientData of each object.
 */
public class ClientHandlerThread extends Thread {

    private final Socket clientSocket;
    private final ClientData clientData;

    private boolean loggedIn;
    private UserData userData;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public ClientHandlerThread(Socket clientSocket, int clientId) {
        this.clientSocket = clientSocket;
        this.clientData = new ClientData(clientId);
    }

    private GpxResults analyzeGpxFile(GpxFile gpxFile) throws InterruptedException {

        this.clientData.setGpxFile(gpxFile);

        LOGGER.info("Client#" + this.clientData.getID() + ": Received GPX File");

        synchronized (GXP_FILE_ID_LOCK) {
            gpxFileId++;
            gpxFile.setGpxFileId(gpxFileId);

            synchronized (INTERMEDIATE_RESULTS_LOCK) {
                intermediateResults.put(gpxFileId, new ArrayList<>());
            }
        }

        gpxFile.makeChunks();
        for (var chunk : gpxFile.getChunks()) {
            synchronized (MESSAGE_Q_LOCK) {
                messageQueue.enqueue(chunk);
                MESSAGE_Q_LOCK.notify();
            }
        }

        int gpxFileId = gpxFile.getGpxFileId();
        // Waiting to receive all intermediate results to perform aggregation and calculate the final result.
        ArrayList<Map.WorkerResult> processedResults;
        synchronized (INTERMEDIATE_RESULTS_LOCK) {
            int intermediateResultsListLength = intermediateResults.get(gpxFileId).size();
            int numberOfChunksInGpx = gpxFile.getChunks().size();
            while (intermediateResultsListLength != numberOfChunksInGpx) {
                INTERMEDIATE_RESULTS_LOCK.wait();
                intermediateResultsListLength = intermediateResults.get(gpxFile.getGpxFileId()).size();
                numberOfChunksInGpx = gpxFile.getChunks().size();
            }
            processedResults = intermediateResults.get(gpxFileId);
        }

        Reduce.ReducedResult finalResults = Reduce.reduce(processedResults);

        double totalDistanceInKilometers = finalResults.value().totalDistanceInKilometers();
        double totalAscentInMeters = finalResults.value().totalAscentInMeters();
        double totalTimeInMinutes = finalResults.value().totalTimeInMinutes();
        double averageSpeedInKilometersPerHour = finalResults.value().averageSpeedKilometerPerHour();
        long totalTimeInMillis = finalResults.value().totalTimeInMillis();


        LOGGER.debug("GPX File ID: " + finalResults.key() +
                ", Total Distance: " + totalDistanceInKilometers +
                ", Total Ascent: " + totalAscentInMeters +
                ", Total Time: " + totalTimeInMinutes +
                ", Average Speed: " + averageSpeedInKilometersPerHour + "\n");


        return new GpxResults(
                totalDistanceInKilometers,
                totalAscentInMeters,
                totalTimeInMinutes,
                averageSpeedInKilometersPerHour,
                totalTimeInMillis
        );
    }

    @Override
    public void run() {
        try {
            loggedIn = false;
            this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
            Gson gson = new Gson();

            Authentication auth = new Authentication();


            while (true) {

                String receivedJsonString = (String) this.inputStream.readObject();
                TransmissionObject receivedData = gson.fromJson(receivedJsonString, TransmissionObject.class);

                if (receivedData.type == TransmissionObjectType.LOGIN_MESSAGE) {
                    try {
                        // See if user exists
                        int userId = auth.handleLoginProcess(receivedData.username, receivedData.password);
                        this.clientData.setUsername(receivedData.username);

                        // if so, log him in
                        loggedIn = true;
                        System.out.println("Login from " + receivedData.username + " successful.");

                        // fetch his user data
                        userData = DataExchangeHandler.getSpecificUserData(userId);

                        // craft a transmission object to send a success message including his data
                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.USER_DATA)
                                .userData(userData)
                                .message("Successful login")
                                .success(1)
                                .craft();

                        // send back to client success message and his user data
                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);

                    } catch (Exception e) {
                        handleAuthFailure(TransmissionObjectType.LOGIN_MESSAGE, e.getMessage(), gson);
                    }
                }

                if (receivedData.type == TransmissionObjectType.LEADERBOARD) {
                    DataExchangeHandler.userData.sort(Comparator.comparing(UserData::getPoints, Collections.reverseOrder()));

                    try {
                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.LEADERBOARD)
                                .leaderboardList(DataExchangeHandler.userData)
                                .message("Leaderboard")
                                .success(1)
                                .craft();

                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);
                    } catch (Exception e) {
                        /*TODO*/
                    }

                }


                if (receivedData.type == TransmissionObjectType.REGISTRATION_MESSAGE) {
                    try {
                        int userId = auth.handleRegistration(receivedData.username, receivedData.password);
                        UserData newUserData = handleNewAccUserData(userId);
                        DataExchangeHandler.userData.add(newUserData);
                        DataExchangeHandler.writeAllUserDataToJson();

                        loggedIn = true;
                        userData = newUserData;
                        userData.username = receivedData.username;

                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.USER_DATA)
                                .userData(userData)
                                .message("Successful Registration")
                                .success(1)
                                .craft();

                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);

                    } catch (Exception e) {
                        handleAuthFailure(TransmissionObjectType.REGISTRATION_MESSAGE, e.getMessage(), gson);
                    }
                }
                if (loggedIn) {
                    if (receivedData.type == TransmissionObjectType.GPX_FILE) {
                        System.out.println("Received gpx file from " + this.clientData.getUsername());
                        GpxFile gpxFile = new GpxFile(new ByteArrayInputStream(receivedData.gpxFile.getBytes()));

                        GpxResults results = analyzeGpxFile(gpxFile);

                        Route newRoute = processGpxResults(results, receivedData);
                        newRoute.routeWaypoints = gpxFile.getWps();
                        newRoute.routeId = userData.routes.size();

                        userData.routes.add(newRoute);
                        userData.points += newRoute.points;
                        userData.routesDoneThisMonth++;
                        userData.totalKmThisMonth += results.distanceInKilometers();
                        userData.totalElevation += results.totalAscentInMete();
                        userData.totalTime += results.totalTimeInMillis();

                        DataExchangeHandler.updateAverageMetrics();

                        DataExchangeHandler.writeAllUserDataToJson();

                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.USER_DATA)
                                .userData(userData)
                                .message("New GPX file has been processed.")
                                .success(1)
                                .craft();

                        to.gpxResults = results;

                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);
                    }
                    // If a user requests to see his stats compared to average population stats, he makes
                    // a separate request to receive fresh and updated data.
                    if (receivedData.type == TransmissionObjectType.GENERIC_STATS_REQUEST) {

                        GenericStats genericStatsObject = new GenericStats();
                        genericStatsObject.totalDistance = userData.totalKmThisMonth;
                        genericStatsObject.totalElevation = userData.totalElevation;
                        genericStatsObject.totalTime = userData.totalTime;
                        populateGenericStatsObject(genericStatsObject);

                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.GENERIC_STATS_REQUEST)
                                .genericStats(genericStatsObject)
                                .message("Request Successful")
                                .success(1)
                                .craft();

                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);
                    }
                    // If a user requests a new state of his data
                    if (receivedData.type == TransmissionObjectType.USER_DATA) {
                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.USER_DATA)
                                .userData(userData)
                                .message("New state of user data")
                                .success(1)
                                .craft();

                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);
                    }

                    // if we receive a new segment
                    if (receivedData.type == TransmissionObjectType.SEGMENT) {

                        GpxFile gpxFile = new GpxFile(
                                (ArrayList<WaypointImpl>) receivedData.userData.routes
                                        .get(receivedData.routeId)
                                        .routeWaypoints
                                        .subList(receivedData.segmentStart, receivedData.segmentEnd)
                        );

                        Segment newSegment = new Segment();
                        newSegment.waypoints = gpxFile.getWps();


                        GpxResults gpxResults = analyzeGpxFile(gpxFile);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static int calculateRoutePoints(GpxResults results) {
        return (int) results.totalAscentInMete() * 10 + (int) results.distanceInKilometers() * 100;
    }

    private UserData handleNewAccUserData(int userId) {
        UserData newUserData = new UserData();
        newUserData.routes = new ArrayList<>();
        newUserData.segments = new ArrayList<>();
        newUserData.userId = userId;
        return newUserData;
    }

    private void handleAuthFailure(TransmissionObjectType type, String message, Gson gson) throws IOException {
        TransmissionObject to = new TransmissionObject();
        to.type = type;
        to.message = message;
        to.success = 0;
        String jsonTransmissionObject = gson.toJson(to);
        outputStream.writeObject(jsonTransmissionObject);
    }

    public Route processGpxResults(GpxResults results, TransmissionObject receivedData) {
        Route newRoute = new Route();
        newRoute.routeName = receivedData.message;
        newRoute.coordinates = receivedData.coordinates;
        newRoute.totalTimeInMinutes = results.totalTimeInMinutes();
        newRoute.totalDistanceInKm = results.distanceInKilometers();
        newRoute.totalElevationInM = results.totalAscentInMete();
        newRoute.averageSpeedInKmH = results.avgSpeedInKilometersPerHour();
        newRoute.routeType = getRouteType(results);
        newRoute.points = calculateRoutePoints(results);

        return newRoute;
    }

    public static int getRouteType(GpxResults results) {
        var ascent = results.totalAscentInMete();
        if (ascent > 200) return 0;
        var speed = results.avgSpeedInKilometersPerHour();
        if (speed > 6.4) return 2;

        return 1;

    }
}
