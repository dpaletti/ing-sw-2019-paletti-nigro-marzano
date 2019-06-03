package it.polimi.se2019.model;


import java.io.IOException;

public class PowerUp extends Card implements Drawable{
    public PowerUp(String path)throws IOException, ClassNotFoundException{
        super(path);
    }
}
