package it.polimi.se2019.model;

import it.polimi.se2019.utility.Log;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class Weapon extends Card implements Grabbable, Drawable,Jsonable{
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

    @Override
    public Jsonable copy() {
        return new Weapon(this);
    }
}
