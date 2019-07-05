package it.polimi.se2019.server.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores hit targets and tiles as some weapons have a memory and their targets vary depending on the
 * targets or tiles hit during a specific effect.
 */

public class TurnMemory {
    private Map<String, List<Player>>  hitTargets= new HashMap<>();
    private  Map<String, List<Tile>> hitTiles= new HashMap<>();
    private String lastEffectUsed= "";

    public TurnMemory(){
        //empty constructor
    }

    public TurnMemory (TurnMemory turnMemory){
        this.hitTiles = turnMemory.getHitTiles();
        this.hitTargets = turnMemory.getHitTargets();
        this.lastEffectUsed = turnMemory.getLastEffectUsed();
    }



    public Map<String, List<Player>> getHitTargets() {
        return new HashMap<>(hitTargets);
    }

    public Map<String, List<Tile>> getHitTiles() {
        return new HashMap<>(hitTiles);
    }


    public void putPlayers(String partialWeaponEffect, List<Player> hitPlayers){
        hitTargets.put(partialWeaponEffect, hitPlayers);

    }

    public void putTiles(String partialWeaponEffect, List<Tile> hitTiles){
        this.hitTiles.put(partialWeaponEffect, hitTiles);
    }

    public void setLastEffectUsed(String lastEffectUsed) {
        this.lastEffectUsed = lastEffectUsed;
    }

    public String getLastEffectUsed() {
        return lastEffectUsed;
    }

    //------------------testing purposes--------------------------

    public void setHitTargets(Map<String, List<Player>> hitTargets) {
        this.hitTargets = hitTargets;
    }

    public void setHitTiles(Map<String, List<Tile>> hitTiles) {
        this.hitTiles = hitTiles;
    }

    //-------------------------------------------------------------

    public void hit (String partialWeaponEffect, List<Targetable> hitTargets, Targetable target){
        target.hit(partialWeaponEffect, hitTargets, this);
        lastEffectUsed=partialWeaponEffect;
    }

    public List<Targetable> getByEffect(List<String> effects, Targetable target){
        return target.getByEffect(effects, this);
    }

    public void end (){
        hitTargets.clear();
        hitTiles.clear();
        lastEffectUsed= "";
    }

}
