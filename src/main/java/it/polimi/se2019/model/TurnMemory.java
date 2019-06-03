package it.polimi.se2019.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurnMemory {
    private Map<String, List<Player>>  hitTargets= new HashMap<>();
    private  Map<String, List<Tile>> hitTiles= new HashMap<>();
    private String lastEffectUsed= "none";

    public TurnMemory(Map<String, List<Player>> hitTargets, Map<String, List<Tile>> hitTiles) {

        this.hitTargets = hitTargets;
        this.hitTiles = hitTiles;
    }

    public TurnMemory (TurnMemory turnMemory){
        this.hitTiles= turnMemory.getHitTiles();
        this.hitTargets= turnMemory.getHitTargets();
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


    public void hit (String partialWeaponEffect, List<? extends Targetable> hitTargets, Targetable target){
        target.hit(partialWeaponEffect, hitTargets, this);
        lastEffectUsed=partialWeaponEffect;
    }

    public List<? extends Targetable> getByEffect(List<String> effects, Targetable target){ //NOSONAR
        return target.getByEffect(effects, this);
    }


    public void hitPlayers (String partialWeaponEffect, List<Player> hitPlayers){
        lastEffectUsed=partialWeaponEffect;
    }

    public void hitTiles (String partialWeaponEffect, List<Tile> hitTiles){
        lastEffectUsed=partialWeaponEffect;
    }

    public void end (){
        hitTargets.clear();
        hitTiles.clear();
        lastEffectUsed= "none";
    }

    public List<Player> getPlayersByEffect (List<String> effects){
        List<Player> hit= new ArrayList<>();
        for (String s: effects){
            hit.addAll(hitTargets.get(s));
        }
        return hit;
    }

    public List<Tile> getTilesByEffect (List<String> effects){
        List<Tile> hit= new ArrayList<>();
        for (String s: effects){
            hit.addAll(hitTiles.get(s));
        }
        return hit;
    }
}
