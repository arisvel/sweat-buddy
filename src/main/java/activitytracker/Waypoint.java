package main.java.activitytracker;

import java.io.Serializable;

public class Waypoint implements Serializable {

    private final int id;
    private final double longitude;
    private final double latitude;
    private final double elevation;
    private final long time;

    public Waypoint(int id, double longitude, double latitude, double elevation, long time) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.time = time;
    }

    public int getID() {
        return this.id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getElevation() {
        return elevation;
    }

    public long getTime() {
        return time;
    }

}
