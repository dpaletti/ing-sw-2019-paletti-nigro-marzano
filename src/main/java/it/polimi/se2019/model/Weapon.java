package it.polimi.se2019.model;

import it.polimi.se2019.utility.Factory;

import java.nio.file.Paths;

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
