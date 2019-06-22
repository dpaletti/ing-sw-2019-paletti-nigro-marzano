package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.utility.Point;

import java.util.Set;

public class Figure {
    private Tile tile;
    private FigureColour colour;
    private Player player;
    private Point position;

    public Figure (FigureColour figureColour){
        this.colour= figureColour;
        position= new Point (-1, -1);
    }

    public Point getPosition() {
        return position;
    }

    public FigureColour getColour() {
        return colour;
    }

    public Tile getTile() {
        return tile;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void damage (Figure target){
        target.player.addTear(colour);
        for (Tear tear: target.player.getMarks()){
            if (tear.getColour().equals(colour)){
                target.player.addTear(colour);
            }
        }
        target.player.updatePlayerDamage();
    }

    public void mark (Figure target){
        int alreadyMaximumMarks=0;
        for(Tear marksCounter: target.player.getMarks()){ //
            if(marksCounter.getColour()==colour){
                alreadyMaximumMarks++;
            }
        }
        if(alreadyMaximumMarks<3) {
            target.player.addMark(colour);
        }
    }

    public void run (Point destination, int distance){
        if (player.getGameMap().getAllowedMovements(tile, distance).contains(destination)){
            position=destination;
            tile=player.getGameMap().getTile(position);
        }
    }

    private Set<Figure> targetSetUpdater (Set<Figure> originalTargetSet, Set<Figure> resultTargetSet, Integer value){
        switch (value){
            case 0:
                originalTargetSet.removeAll(resultTargetSet);
                break;
            case 1:
                for (Figure figureCounter: originalTargetSet){
                    if(!resultTargetSet.contains(figureCounter)){
                        originalTargetSet.remove(figureCounter);
                    }
                }
                break;
            case 2:
                originalTargetSet.clear();
                originalTargetSet.add(this);
                break;
            default:
                break;
        }
        return originalTargetSet;
    }

    private Set<Tile> tileSetUpdater (Set<Tile> originalTargetSet, Set<Tile> resultTargetSet, Integer value){
        switch (value){
            case 0:
                originalTargetSet.removeAll(resultTargetSet);
                break;
            case 1:
                for (Tile tileCounter: originalTargetSet){
                    if(!resultTargetSet.contains(tileCounter)){
                        originalTargetSet.remove(tileCounter);
                    }
                }
                break;
            case 2:
                originalTargetSet.clear();
                originalTargetSet.add(this.tile);
                break;
            default:
                break;
        }
        return originalTargetSet;
    }

    public void grab(String grabbed){
        for (Grabbable g : tile.getGrabbables())
            g.grab(player, grabbed);
    }

    public void reload (Weapon weapon){
        int enoughAmmoForReload=0;
        for (Ammo reloadCostCounter: weapon.getPrice()) {   //checks whether the reload price can be payed
            for (Ammo availableAmmoCounter: player.getAmmo()){
                if(reloadCostCounter==availableAmmoCounter){
                    enoughAmmoForReload++;
                    break;
                }
            }

        }
        if(enoughAmmoForReload==weapon.getPrice().size()){  //in case player has enough ammo to pay for the reload
            weapon.setLoaded(true);
            player.useAmmos(weapon.getPrice());
        }
        else{
            NotEnoughAmmoEvent notEnoughAmmoEvent=null;
             //not enough ammo available to reload, want to use PowerUps?
            //TODO: VCEvent: no, end reload/yes, use selectedPowerUp to pay
            PowerUp selectedPowerUp=null;
            player.sellPowerUp(selectedPowerUp.name);
            reload(weapon);
        }
    }

    //to fix

    /*public void shoot (PartialWeaponEffect partialWeaponEffect, FigureColour figureColour){
        for (Action action: partialWeaponEffect.getActions()){

        }
    }*/
}