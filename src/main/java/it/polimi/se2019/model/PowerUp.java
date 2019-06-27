package it.polimi.se2019.model;


import it.polimi.se2019.utility.Log;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class PowerUp extends Card implements Drawable,Jsonable{
    public PowerUp(String path){
        super(path);
    }

    public PowerUp(PowerUp powerUp){
        super(powerUp);
    }

}
