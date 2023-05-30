package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import dependencies.fileprocessing.TransmissionObject;
import dependencies.fileprocessing.TransmissionObjectType;
import dependencies.fileprocessing.gpx.GpxResults;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import dependencies.Utilities;
import dependencies.fileprocessing.gpx.GpxFile;

public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private String username;
//    private GpxFile gpxFile;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private Gson gson = new Gson();

    public Client(String host_address, int port) {

        try {
            this.socket = new Socket(host_address, port);

            this.outputStream = new ObjectOutputStream(socket.getOutputStream());

            this.inputStream = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendClientInfo(TransmissionObject to) {
        try {
            String jsonString = gson.toJson(to);

            this.outputStream.writeObject(jsonString);
            this.outputStream.flush();


        } catch (IOException e) {
            System.out.println("error");
        }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Client c = new Client(Utilities.HOST_ADDRESS, Utilities.CLIENTS_PORT);

        Scanner s = new Scanner(System.in);
        System.out.print("Username: ");
        String username = s.nextLine();
        System.out.println("Password: ");
        String password = s.nextLine();

        TransmissionObject to = new TransmissionObject();
        to.type = TransmissionObjectType.REGISTRATION_MESSAGE;
        to.username = username;
        to.password = password;
        c.sendClientInfo(to);

        String answer = (String) c.inputStream.readObject();
        System.out.println(answer);

        TimeUnit.SECONDS.sleep(2);

        TransmissionObject to2 = new TransmissionObject();
        to2.type = TransmissionObjectType.GPX_FILE;
        to2.gpxFile = gpxFile;
        c.sendClientInfo(to2);
        String answer2 = (String) c.inputStream.readObject();
        System.out.println(answer2);
        while (true) {

        }

    }

    static String gpxFile = """
            <?xml version="1.0"?>
            <gpx version="1.1" creator="user1">
            <wpt lat="37.95001155239993" lon="23.69503479744284">
                <ele>12.08</ele>
                <time>2023-03-19T17:36:01Z</time>
            </wpt>
            <wpt lat="37.949571620793144" lon="23.695699985278534">
                <ele>9.98</ele>
                <time>2023-03-19T17:36:31Z</time>
            </wpt>
            <wpt lat="37.94923321007226" lon="23.696236427081512">
                <ele>8.90</ele>
                <time>2023-03-19T17:36:55Z</time>
            </wpt>
            <wpt lat="37.948674828974916" lon="23.69680505539267">
                <ele>9.02</ele>
                <time>2023-03-19T17:37:26Z</time>
            </wpt>
            <wpt lat="37.94788801204387" lon="23.69558196808188">
                <ele>6.58</ele>
                <time>2023-03-19T17:38:21Z</time>
            </wpt>
            <wpt lat="37.94715194986727" lon="23.694605644000458">
                <ele>4.69</ele>
                <time>2023-03-19T17:39:08Z</time>
            </wpt>
            <wpt lat="37.946653163250694" lon="23.695273887184726">
                <ele>5.46</ele>
                <time>2023-03-19T17:39:40Z</time>
            </wpt>
            <wpt lat="37.94606938059409" lon="23.696046363381015">
                <ele>5.77</ele>
                <time>2023-03-19T17:40:17Z</time>
            </wpt>
            <wpt lat="37.94524023203743" lon="23.695102225807773">
                <ele>4.37</ele>
                <time>2023-03-19T17:41:06Z</time>
            </wpt>
            <wpt lat="37.944461839163594" lon="23.694050799873935">
                <ele>3.80</ele>
                <time>2023-03-19T17:41:56Z</time>
            </wpt>
            <wpt lat="37.943697486759284" lon="23.69324249390282">
                <ele>2.53</ele>
                <time>2023-03-19T17:42:40Z</time>
            </wpt>
            <wpt lat="37.943405584269506" lon="23.69371456268944">
                <ele>2.34</ele>
                <time>2023-03-19T17:43:01Z</time>
            </wpt>
            <wpt lat="37.943189829510054" lon="23.694036427771227">
                <ele>2.28</ele>
                <time>2023-03-19T17:43:15Z</time>
            </wpt>
            <wpt lat="37.942627171961284" lon="23.694884005819933">
                <ele>2.40</ele>
                <time>2023-03-19T17:43:53Z</time>
            </wpt>
            <wpt lat="37.94196891990529" lon="23.695816839239242">
                <ele>2.56</ele>
                <time>2023-03-19T17:44:36Z</time>
            </wpt>
            <wpt lat="37.941774313796756" lon="23.695747101804855">
                <ele>3.57</ele>
                <time>2023-03-19T17:44:44Z</time>
            </wpt>
            <wpt lat="37.941499326025664" lon="23.695532525083664">
                <ele>3.08</ele>
                <time>2023-03-19T17:44:58Z</time>
            </wpt>
            <wpt lat="37.94130471867361" lon="23.695323312780502">
                <ele>2.80</ele>
                <time>2023-03-19T17:45:09Z</time>
            </wpt>
            <wpt lat="37.94097984389345" lon="23.694886713748932">
                <ele>2.67</ele>
                <time>2023-03-19T17:45:30Z</time>
            </wpt>
            <wpt lat="37.94045524527583" lon="23.69421079707718">
                <ele>2.62</ele>
                <time>2023-03-19T17:46:03Z</time>
            </wpt>
            <wpt lat="37.94014217657466" lon="23.693808465724945">
                <ele>2.68</ele>
                <time>2023-03-19T17:46:22Z</time>
            </wpt>
            <wpt lat="37.93978679967615" lon="23.693347125774384">
                <ele>2.67</ele>
                <time>2023-03-19T17:46:44Z</time>
            </wpt>
            <wpt lat="37.93952161016134" lon="23.69299274828799">
                <ele>3.70</ele>
                <time>2023-03-19T17:47:01Z</time>
            </wpt>
            <wpt lat="37.93923603716365" lon="23.692619921234918">
                <ele>5.81</ele>
                <time>2023-03-19T17:47:19Z</time>
            </wpt>
            <wpt lat="37.9387847281908" lon="23.692015802071865">
                <ele>4.86</ele>
                <time>2023-03-19T17:47:48Z</time>
            </wpt>
            <wpt lat="37.93887357378787" lon="23.691527640031154">
                <ele>2.31</ele>
                <time>2023-03-19T17:48:05Z</time>
            </wpt>
            <wpt lat="37.93877203595387" lon="23.691431080506618">
                <ele>2.09</ele>
                <time>2023-03-19T17:48:10Z</time>
            </wpt>
            <wpt lat="37.93911472558143" lon="23.690717612908657">
                <ele>2.47</ele>
                <time>2023-03-19T17:48:39Z</time>
            </wpt>
            <wpt lat="37.939673179328615" lon="23.690224610251718">
                <ele>2.92</ele>
                <time>2023-03-19T17:49:09Z</time>
            </wpt>
            <wpt lat="37.94003701812341" lon="23.690106593055063">
                <ele>2.97</ele>
                <time>2023-03-19T17:49:25Z</time>
            </wpt>
            <wpt lat="37.94053200521952" lon="23.6907074078744">
                <ele>2.16</ele>
                <time>2023-03-19T17:49:55Z</time>
            </wpt>
            <wpt lat="37.94081968849565" lon="23.69096489993983">
                <ele>2.25</ele>
                <time>2023-03-19T17:50:10Z</time>
            </wpt>
            <wpt lat="37.94162423069252" lon="23.69053836283811">
                <ele>2.57</ele>
                <time>2023-03-19T17:50:48Z</time>
            </wpt>
            <wpt lat="37.942208048664426" lon="23.691133813239418">
                <ele>2.89</ele>
                <time>2023-03-19T17:51:21Z</time>
            </wpt>
            <wpt lat="37.94297477498236" lon="23.69215686087734">
                <ele>3.00</ele>
                <time>2023-03-19T17:52:10Z</time>
            </wpt>
            <wpt lat="37.94371087900379" lon="23.69313854937679">
                <ele>2.58</ele>
                <time>2023-03-19T17:52:57Z</time>
            </wpt>
            <wpt lat="37.944477889945155" lon="23.693989646908282">
                <ele>3.79</ele>
                <time>2023-03-19T17:53:42Z</time>
            </wpt>
            <wpt lat="37.94513360173761" lon="23.693201077457903">
                <ele>2.99</ele>
                <time>2023-03-19T17:54:22Z</time>
            </wpt>
            </gpx>
                        
            """;
}
