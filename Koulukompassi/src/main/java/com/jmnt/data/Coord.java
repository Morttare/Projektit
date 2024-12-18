package com.jmnt.data;

/**
 * Represents geographical coordinates with latitude and longitude values.
 */
public class Coord {
    private double lat;
    private double lon;

    /**
     * Constructs a new Coord object with the specified latitude and longitude.
     *
     * @param lat the latitude of the coordinate.
     * @param lon the longitude of the coordinate.
     */
    public Coord(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
