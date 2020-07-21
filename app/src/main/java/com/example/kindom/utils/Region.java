package com.example.kindom.utils;

/**
 * Represent the different regions of Singapore
 */
public class Region {

    public static final String NORTH = "north";
    public static final String SOUTH = "south";
    public static final String EAST = "east";
    public static final String WEST = "west";
    public static final String CENTRAL = "central";

    /**
     * Retrieve the region the user is staying in from the postal code
     *
     * @param postalCode the user's postal code
     * @return a string depicting the region the user is staying in
     */
    public static String getRegion(int postalCode) {
        int firstTwoDigits = postalCode / 10000;
        if (firstTwoDigits == 53 || firstTwoDigits == 54 || firstTwoDigits == 55 ||
                firstTwoDigits == 72 || firstTwoDigits == 73 ||
                firstTwoDigits == 75 || firstTwoDigits == 76 ||
                firstTwoDigits == 79 ||firstTwoDigits == 80 ||
                firstTwoDigits ==82) {
            return NORTH;
        } else if ((firstTwoDigits >= 1 && firstTwoDigits <= 10) ||
                (firstTwoDigits >= 14 && firstTwoDigits <= 30)) {
            return SOUTH;
        } else if ((firstTwoDigits >= 38 && firstTwoDigits <= 52) ||
                firstTwoDigits == 81) {
            return EAST;
        } else if ((firstTwoDigits >= 11 && firstTwoDigits <= 13) ||
                (firstTwoDigits >= 58 && firstTwoDigits <= 71)) {
            return WEST;
        } else {
            return CENTRAL;
        }
    }
}