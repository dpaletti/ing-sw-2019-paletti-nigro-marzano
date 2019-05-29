package it.polimi.se2019.model;

import it.polimi.se2019.utility.Factory;
import it.polimi.se2019.utility.Log;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

//Singleton class with all the static weapons and powerUps and methods to find them by name
public class CardHelper {
    private static CardHelper instance=null;
    private static Set<Weapon> allWeapons= new HashSet<>();
    private static Set<PowerUp> allPowerUp= new HashSet<>();
    private CardHelper(){
        Log.fine( Paths.get("files/powerUps").toFile().list().toString());
        for (String name : Paths.get( "files/weapons").toFile().list()) {
                allWeapons.add((Factory.createWeapon(Paths.get("files/weapons/".concat(name)).toString())));
        }
        for (String name : Paths.get("files/powerUps").toFile().list()) {
                allPowerUp.add(Factory.createPowerUp(Paths.get("files/powerUps/".concat(name)).toString()));
        }
    }

    public static CardHelper getInstance() {
        if (instance == null){
            instance= new CardHelper();
        }
        return instance;
    }

    public PowerUp findPowerUpByName(String name, AmmoColour colour) {
        for (PowerUp powerUp : allPowerUp) {
            if (powerUp.getName().equals(name) && powerUp.getCardColour().getColour().equals(colour)) {
                return powerUp;
            }
        }
        throw new NullPointerException("PowerUp not found");
    }

    public Weapon findWeaponByName(String name){
        for (Weapon weapon : allWeapons){
            if (weapon.getName().equals(name)){
                return weapon;
            }
        }
        throw new NullPointerException("Weapon not found");
    }

    public Set<PowerUp> getAllPowerUp() {
        return allPowerUp;
    }

    public Set<Weapon> getAllWeapons() {
        return allWeapons;
    }
}
