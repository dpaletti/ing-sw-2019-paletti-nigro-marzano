package it.polimi.se2019.server.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class defines an ammo which consists of its colour.
 */

public class Ammo implements Serializable {
    private AmmoColour colour;

    public void setColour(AmmoColour colour) {
        this.colour = colour;
    }

    public AmmoColour getColour() {
        return colour;
    }

    public Ammo(AmmoColour colour){
        this.colour= colour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ammo ammo = (Ammo) o;
        return getColour() == ammo.getColour();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColour());
    }
}
