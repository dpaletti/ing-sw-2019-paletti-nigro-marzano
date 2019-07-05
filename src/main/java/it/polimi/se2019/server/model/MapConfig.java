package it.polimi.se2019.server.model;

/**
 * This class defines the map that are built form the JSON files. Each map has a name and the name of its two halves.
 */

public class MapConfig {
    private String name;
    private String leftHalf;
    private String rightHalf;

    public String getName() {
        return name;
    }

    public String getLeftHalf() {
        return leftHalf;
    }

    public String getRightHalf() {
        return rightHalf;
    }
}
