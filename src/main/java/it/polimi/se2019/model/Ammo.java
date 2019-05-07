package it.polimi.se2019.model;

public class Ammo {
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
