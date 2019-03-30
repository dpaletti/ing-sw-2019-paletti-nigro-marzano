package it.polimi.se2019.model;

public class Skull {

    private Tear tear;
    private Boolean overkill;

    public Boolean getOverkill() {
        return overkill;
    }

    public Tear getTear() {
        return tear;
    }

    public void setOverkill(Boolean overkill) {
        this.overkill = overkill;
    }

    public void setTear(Tear tear) {
        this.tear = tear;
    }
}
