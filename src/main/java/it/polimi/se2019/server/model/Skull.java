package it.polimi.se2019.server.model;

/**
 * This class defines a skull which is empty until a user dies. When this happens, a skull is created with the colour of the
 * killer and a boolean that signals whether the player was overkilled.
 */

public class Skull {

    private Tear tear;
    private Boolean overkill;

    public Skull (Tear tear, Boolean overkill){
        this.tear= tear;
        this.overkill= overkill;
    }
    public Boolean getOverkill() {
        return overkill;
    }

    public Tear getTear() {
        return tear;
    }

}
