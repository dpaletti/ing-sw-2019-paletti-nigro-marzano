package it.polimi.se2019.server.model;

import java.util.Objects;

/**
 * This class defines a tear, which represents the damage or mark element given by a player to another.
 * A tear is defined by the colour of its owner.
 */

public class Tear {
    private FigureColour colour;

    public Tear (FigureColour colour){ this.colour=colour; }

    public FigureColour getColour() { return colour; }

    @Override
    public String toString() {
        return colour.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tear tear = (Tear) o;
        return colour == tear.colour;
    }

    @Override
    public int hashCode() {
        return Objects.hash(colour);
    }
}
