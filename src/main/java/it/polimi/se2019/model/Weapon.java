package it.polimi.se2019.model;

import java.io.IOException;

public class Weapon extends Card implements Grabbable, Drawable{
    private Boolean loaded = true;

    public Boolean getLoaded() {
        return loaded;
    }

    public Weapon (String path) throws IOException, ClassNotFoundException{
            super(path);
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

}
