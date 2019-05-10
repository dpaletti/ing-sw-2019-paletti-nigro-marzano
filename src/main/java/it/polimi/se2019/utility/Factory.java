package it.polimi.se2019.utility;

import it.polimi.se2019.model.PowerUp;
import it.polimi.se2019.model.Weapon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Factory {
    private Factory(){}

    //TODO Test it and see if it's good to return a list of objects
    private static Object create(Path directory){
        try {
            return JsonHandler.deserialize(new String(Files.readAllBytes(directory)));
        }catch (NullPointerException e){
            Log.severe("Such directory does not exists");
        }catch (IOException e){
            Log.severe("IO exception reading from the stream");
        }catch (ClassNotFoundException e){
            Log.severe("Class is not correctly inserted in the JSON file");
        }
        return null;
    }

    public static Weapon createWeapon(String path){
        Weapon weapon;
        weapon= (Weapon)Factory.create(Paths.get(path));
        return weapon;
    }

    public static PowerUp createPowerUp(String path){
        PowerUp powerUp;
        powerUp= (PowerUp)Factory.create(Paths.get(path));
        return powerUp;
    }
}
