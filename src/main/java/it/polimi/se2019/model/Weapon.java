package it.polimi.se2019.model;

public class Weapon extends Card{

    private Weapon(String name, AmmoColour ammoColour){
        super(name, ammoColour);
    }

    private Boolean loaded;

    public Boolean getLoaded() {
        return loaded;
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

}
