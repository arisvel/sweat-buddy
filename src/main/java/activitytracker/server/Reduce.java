package main.java.activitytracker.server;

import main.java.activitytracker.fileprocessing.Haversine;
import main.java.activitytracker.worker.Map;

import java.io.Serializable;
import java.util.ArrayList;

public class Reduce {

    public static ReducedResult reduce(ArrayList<Map.WorkerResult> workerResults) {

        workerResults.sort(new Map.WorkerResultComparator());
        Map.WorkerResult currentChunkResults;
        Map.WorkerResult nextChunkResults;

        long totalTime = 0;
        double totalAscent = 0;
        double totalDistance = 0;

        for (int i = 1; i <= workerResults.size(); i++) {
            
            currentChunkResults = workerResults.get(i - 1);
            
            Map.ChunkTimeData currentChunkTimeData = currentChunkResults.value().chunkTimeData();

            Map.ChunkAscentData currentChunkAscentData = currentChunkResults.value().chunkAscentData();

            Map.ChunkDistanceData currentChunkDistanceData = currentChunkResults.value().chunkDistanceData();

            totalTime += currentChunkTimeData.chunkTime();

            totalAscent += currentChunkAscentData.ascent();

            totalDistance += currentChunkDistanceData.distance();

            if (i < workerResults.size())
            {
                nextChunkResults = workerResults.get(i);

                Map.ChunkTimeData nextChunkTimeData = nextChunkResults.value().chunkTimeData();

                Map.ChunkAscentData nextChunkAscentData = nextChunkResults.value().chunkAscentData();

                Map.ChunkDistanceData nextChunkDistanceData = nextChunkResults.value().chunkDistanceData();

                totalTime += (nextChunkTimeData.firstWaypointTime() - currentChunkTimeData.lastWaypointTime());

                totalAscent += Math.max(0, (nextChunkAscentData.firstWaypointElevation() - currentChunkAscentData.lastWaypointElevation()));

                totalDistance += Haversine.distance(
                        nextChunkDistanceData.firstWaypointLat(),
                        nextChunkDistanceData.firstWaypointLong(),
                        currentChunkDistanceData.lastWaypointLat(),
                        currentChunkDistanceData.lastWaypointLong()
                );
            }

     
        }

        double averageSpeed = totalDistance / totalTime;

        return new ReducedResult(
                new Key(workerResults.get(0).key().gpxFileId()),
                new Value(totalDistance, totalAscent, totalTime, averageSpeed)
        );
    }


    public record ReducedResult(Key key, Value value) implements Serializable {
    }

    public record Key(int gpxFileId) implements Serializable {
    }

    public record Value(double totalDistance, double totalAscent, long totalTime,
                        double averageSpeed) implements Serializable {
    }
}
