package it.polimi.se2019.server.model;

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
