package com.jmnt.data;

/**
 * Represents information about a campus, including its geographical coordinates and type.
 */
public class CampusInfo {
    private Coord coord;
    private String type;


    /**
     * Constructs a CampusInfo object with the specified coordinates and type.
     *
     * @param coord the geographical coordinates of the campus.
     * @param type  the type of the campus.
     */
    public CampusInfo(Coord coord, String type) {
        this.coord = coord;
        this.type = type;
    }


    /**
     * Retrieves the geographical coordinates of the campus.
     *
     * @return the coordinates of the campus.
     */
    public Coord getCoord() {
        return coord;
    }


    /**
     * Retrieves the type of the campus.
     *
     * @return the type of the campus as a string.
     */
    public String getType() {
        return type;
    }
}
