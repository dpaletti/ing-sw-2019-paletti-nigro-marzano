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
    private static Set<LootCard> allLootCards= new HashSet<>();
    private CardHelper(){
        Log.fine( Paths.get("files/powerUps").toFile().list().toString());
        for (String name : Paths.get( "files/weapons").toFile().list()) {
                allWeapons.add((Factory.createWeapon(Paths.get("files/weapons/".concat(name)).toString())));
        }
        for (String name : Paths.get("files/powerUps").toFile().list()) {
                allPowerUp.add(Factory.createPowerUp(Paths.get("files/powerUps/".concat(name)).toString()));
        }
        for (AmmoColour topAmmoColour: AmmoColour.values()){
            for (AmmoColour bottomAmmoColour: AmmoColour.values()){
                if (!topAmmoColour.equals(bottomAmmoColour)) {
                    for (int i = 0; i < 3; i++) {
                        char[] name = {topAmmoColour.toString().charAt(0),
                                bottomAmmoColour.toString().charAt(0),
                                bottomAmmoColour.toString().charAt(0)};
                        String nameOfCard = new String(name);
                        LootCard lootCard = new LootCard(nameOfCard, topAmmoColour, bottomAmmoColour, bottomAmmoColour);
                        allLootCards.add(lootCard);
                    }
                }
                char[] name = {'P',
                                topAmmoColour.toString().charAt(0),
                                bottomAmmoColour.toString().charAt(0)};
                String nameOfCard= new String(name);
                LootCard lootCard= new LootCard(nameOfCard, topAmmoColour, bottomAmmoColour);
                allLootCards.add(lootCard);
            }
        }
    }

    public static CardHelper getInstance() {
        if (instance == null){
            instance= new CardHelper();
        }
        return instance;
    }

    public PowerUp findPowerUpByName(String name) {
        for (PowerUp powerUp : allPowerUp) {
            if (name.equalsIgnoreCase(powerUp.getName())) {
                return powerUp;
            }
        }
        throw new NullPointerException("PowerUp not found");
    }

    public Weapon findWeaponByName(String name){
        for (Weapon weapon : allWeapons){
            if (weapon.getName().equalsIgnoreCase(name)){
                return weapon;
            }
        }
        throw new NullPointerException("Weapon not found");
    }

    public LootCard findLootCardByName (String name){
        for (LootCard lootCard: allLootCards){
            if (lootCard.getName().equalsIgnoreCase(name)){
                return lootCard;
            }
        }
        throw new NullPointerException("LootCard not found");
    }

    public Set<PowerUp> getAllPowerUp() {
        return allPowerUp;
    }

    public Set<Weapon> getAllWeapons() {
        return allWeapons;
    }

    public Set<LootCard> getAllLootCards(){
        return allLootCards;
    }
}
