package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.utility.Log;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * This class generates the Power Up from the JSON files present in the given directory.
 */

public class PowerUpHelper extends JsonHelper{
    public PowerUpHelper() {
        this.create();
    }
    public PowerUpHelper(PowerUpHelper power){
        this.helped=power.helped;
    }

    @Override
    public void create() {
        Set<Jsonable> allPowerUps=new HashSet<>();
        try {
            for (String n : Paths.get("files/powerUps").toFile().list()) {
                helped.add((new PowerUp(Paths.get("files/powerUps/".concat(n)).toString())));
            }
        }catch (NullPointerException e){
            Log.severe("PowerUp not found in the powerUp directory");
        }
    }

    public Set<PowerUp> getPowerUps(){
        Set<PowerUp> powerUps= new HashSet<>();
        for (Jsonable j: this.getAll()){
            powerUps.add((PowerUp)j);
        }
        return powerUps;
    }
}
