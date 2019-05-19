package it.polimi.se2019.model;

import java.util.Set;

public class CardHelper {
    private CardHelper(){}

    public static PowerUp findPowerUpByName(String name, Set<PowerUp> set, AmmoColour colour) {
        for (PowerUp powerUp : set) {
            if (powerUp.getName().equals(name) && powerUp.getCardColour().getColour().equals(colour)) {
                return powerUp;
            }
        }
        throw new NullPointerException("PowerUp not found");
    }

    public static Weapon findWeaponByName(String name, Set<Weapon> set){
        for (Weapon weapon : set){
            if (weapon.getName().equals(name)){
                return weapon;
            }
        }
        throw new NullPointerException("Weapon not found");
    }


}
