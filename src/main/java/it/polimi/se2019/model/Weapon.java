package it.polimi.se2019.model;

public class Weapon extends Card{
    private Weapon(){}

    private Boolean loaded;

    public Boolean getLoaded() {
        return loaded;
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

}
