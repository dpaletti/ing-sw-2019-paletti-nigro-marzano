package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.utility.Log;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * This class generates all the available weapons fetching their JSONs from the defined folder of weapons.
 */

public class WeaponHelper extends JsonHelper{

    public WeaponHelper() {
        this.create();
    }

    public WeaponHelper(WeaponHelper weaponHelper){
        this.helped=weaponHelper.helped;
    }

    @Override
    public void create() {
        Set<Jsonable> allWeapons=new HashSet<>();
        try {
            for (String n : Paths.get("files/weapons").toFile().list()) {
                helped.add((new Weapon(Paths.get("files/weapons/".concat(n)).toString())));
            }
        }catch (NullPointerException e){
            Log.severe("Weapon not found in the weapons directory");
        }
    }

    public Set<Weapon> getWeapons(){
        Set<Weapon> weapons= new HashSet<>();
        for (Jsonable j: this.getAll()){
            weapons.add((Weapon)j);
        }
        return weapons;
    }
}
