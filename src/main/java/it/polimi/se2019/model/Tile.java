package it.polimi.se2019.model;

import java.util.Map;
import java.util.Set;

public abstract class Tile {
    private RoomColour colour;
    private Map<Direction, Boolean> doors;
    private Set<Figure> figures;

    public Object grab();

    public void setColour(RoomColour colour) {
        this.colour = colour;
    }

    public void setDoors(Map<Direction, Boolean> doors) {
        this.doors = doors;
    }

    public void setFigures(Set<Figure> figures) {
        this.figures = figures;
    }

    public RoomColour getColour() {
        return colour;
    }

    public Map<Direction, Boolean> getDoors() {
        return doors;
    }

    public Set<Figure> getFigures() {
        return figures;
    }
}
