package it.polimi.se2019.model;

public class Weapon extends Card{
    private Boolean loaded;
    private String weaponType;

    public Boolean getLoaded() {
        return loaded;
    }

    public String getWeaponType() {
        return weaponType;
    }

    private Weapon(String name, AmmoColour ammoColour){
        super(name, ammoColour);
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

}
