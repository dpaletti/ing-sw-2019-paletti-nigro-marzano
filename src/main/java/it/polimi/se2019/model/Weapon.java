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

    //check number of weapons in hand
    //if < 3, pay cost and add weapon to list
    //else send choice event to game

    @Override
    public void grab(Player player, String grabbed) {
        int index =-1;
        for (Grabbable w : player.getFigure().getTile().getGrabbables()){
            if (w.getName().equalsIgnoreCase(grabbed)) {
               index = player.getFigure().getTile().getGrabbables().indexOf(w);
               break;
            }
        }
        if (index == -1)
            throw new UnsupportedOperationException(grabbed + "\t is not in the current weapon spot and cannot be grabbed");
        if (player.getWeapons().size() < 3){
            if (player.pay(((Weapon)player.getFigure().getTile().grabbables.get(index)).price))   //price could be and was paid
                player.addWeapon((Weapon)player.getFigure().getTile().grabbables.get(index));
        }
    }


}
