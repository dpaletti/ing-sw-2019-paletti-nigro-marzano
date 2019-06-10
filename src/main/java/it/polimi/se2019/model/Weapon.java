package it.polimi.se2019.model;

public class Weapon extends Card implements Grabbable, Drawable{
    private Boolean loaded = true;

    public Boolean getLoaded() {
        return loaded;
    }

    public Weapon (String path){
            super(path);
    }

    public Weapon(Weapon weapon){
        super(weapon);
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

}
