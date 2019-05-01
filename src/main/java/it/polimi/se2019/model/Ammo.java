package it.polimi.se2019.model;

import java.io.Serializable;

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
}
