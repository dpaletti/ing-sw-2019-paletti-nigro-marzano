package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.utility.Point;

import java.util.HashSet;
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

    public void damage(Figure target){
        target.player.addTear(colour);
        for (Tear tear: target.player.getMarks()){
            if (tear.getColour().equals(colour)){
                target.player.addTear(colour);
            }
        }
        target.player.updatePlayerDamage();
    }

    public void mark(Figure target){
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

    public void move(Direction direction){
        Point newPosition= new Point(tile.getPosition().getX(), tile.getPosition().getY());
        switch (direction){
            case EAST:
                newPosition.setX(newPosition.getX()+1);
                break;
            case WEST:
                newPosition.setX(newPosition.getX()-1);
                break;
            case NORTH:
                newPosition.setY(newPosition.getY()+1);
                break;
            case SOUTH:
                newPosition.setY(newPosition.getY()-1);
                break;
                default:
                    break;
        }
        if (boundaryChecker(this, newPosition)){
            position=newPosition;
            tile=player.getGameMap().getMap().get(newPosition);
        }
    } //MVEvent to notify final position to all users

    public void move(Figure target, Direction direction){
        Point newPosition= new Point(target.tile.getPosition().getX(), target.tile.getPosition().getY());
        switch (direction){
            case EAST:
                newPosition.setY(newPosition.getY()+1);
                break;
            case WEST:
                newPosition.setY(newPosition.getY()-1);
                break;
            case NORTH:
                newPosition.setX(newPosition.getX()+1);
                break;
            case SOUTH:
                newPosition.setX(newPosition.getX()-1);
                break;
                default:
                    break;
        }
        if (boundaryChecker(target, newPosition)) {
            target.position = newPosition;
            target.tile = player.getGameMap().getMap().get(newPosition);
        }
    } //MVEvent to notify final position to all users

    public void teleport (Point teleportPosition){ //only called in case of Teleport Event
        if (boundaryChecker(this, teleportPosition)){
            position= teleportPosition;
            tile=player.getGameMap().getMap().get(position);
        }
    }

    public void run (Point destination){
        if ((destination.getDistance(tile.position)<=3)&&(boundaryChecker(this, destination))){
            position=destination;
            tile=player.getGameMap().getMap().get(position);
        }
    }

    public void damage (SpawnTile target){
        if (target.getTileType()==TileType.SPAWNTILE){
            target.addTear(colour);
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

    private Boolean boundaryChecker (Figure figureToMove, Point newPosition){
        if (!player.getGameMap().checkBoundaries(newPosition)){
            return (false);
        }
        if (player.getGameMap().getMap().get(newPosition).getColour().equals(figureToMove.tile.colour)){ //same room, figure can be moved
            return (true);
        }
        else { //not same room, check if door
            Point positionCounter= figureToMove.position;
            for (Direction directionCounter: Direction.values()){
                if (figureToMove.tile.doors.get(directionCounter)){
                    if (directionCounter.equals(Direction.NORTH)){
                        positionCounter.setY(positionCounter.getY()+1);
                        if (positionCounter.equals(newPosition)){
                            return (true);
                        }
                    }
                    if (directionCounter.equals(Direction.SOUTH)){
                        positionCounter.setY(positionCounter.getY()-1);
                        if (positionCounter.equals(newPosition)){
                            return (true);
                        }
                    }
                    if (directionCounter.equals(Direction.EAST)){
                        positionCounter.setX(positionCounter.getX()+1);
                        if (positionCounter.equals(newPosition)){
                            return (true);
                        }
                    }
                    if (directionCounter.equals(Direction.WEST)){
                        positionCounter.setX(positionCounter.getX()-1);
                        if (positionCounter.equals(newPosition)){
                            return (true);
                        }
                    }
                }
            }
        }
        return (false);
    }

    public void grab(Card grabbed){ //TODO: modify grab with parameter (grabbed card)
        if (tile.getTileType()==TileType.LOOTTILE) { //when on a Loot Tile, adds grabbed ammo to usable ammo making sure the number of ammo of a colour does not exceed 3
            int ammoOfSelectedColour=0;
            for (Ammo lootCounter : tile.getLootCard().getAmmo()) {
                for (Ammo ammoCounter: player.getUsableAmmo()){
                    if (ammoCounter==lootCounter) {
                        ammoOfSelectedColour++;
                    }
                }
                if (ammoOfSelectedColour<3) {
                    player.getUsableAmmo().add(lootCounter);
                    player.getUnusableAmmo().remove(lootCounter);
                }
                ammoOfSelectedColour=0;
            }
        }
        else {  //when on a Spawn Tile, allows player to grab a weapon and leave one among those they previously grabbed if they already own 3
            Weapon selectedWeapon= null;
            Set<String> availableWeapons= new HashSet<>();
            availableWeapons.add(tile.getWeaponSpot().getFirstWeapon().getName());
            availableWeapons.add(tile.getWeaponSpot().getSecondWeapon().getName());
            availableWeapons.add(tile.getWeaponSpot().getThirdWeapon().getName());
            // TODO: vc_events: a Weapon is returned and assigned to selectedWeapon

            if (player.getFirstWeapon()==null){
                player.setFirstWeapon(selectedWeapon);
            }
            else if (player.getSecondWeapon()==null){
                player.setSecondWeapon(selectedWeapon);
            }

            else if (player.getThirdWeapon()==null){
                player.setThirdWeapon(selectedWeapon);
            }

            else {

                //TODO: vc_events: a weapon is returned and selectedWeapon is assigned to free weapon slot
            }
        }
    }

    public void reload (Weapon weapon){
        int enoughAmmoForReload=0;
        for (Ammo reloadCostCounter: weapon.getPrice()) {   //checks whether the reload price can be payed
            for (Ammo availableAmmoCounter: player.getUsableAmmo()){
                if(reloadCostCounter==availableAmmoCounter){
                    enoughAmmoForReload++;
                    break;
                }
            }

        }
        if(enoughAmmoForReload==weapon.getPrice().size()){  //in case player has enough ammo to pay for the reload
            weapon.setLoaded(true);
            player.useAmmo(weapon.getPrice());
        }
        else{
            NotEnoughAmmoEvent notEnoughAmmoEvent=null;
             //not enough ammo available to reload, want to use PowerUps?
            //TODO: VCEvent: no, end reload/yes, use selectedPowerUp to pay
            PowerUp selectedPowerUp=null;
            player.sellPowerUp(selectedPowerUp);
            reload(weapon);
        }
    }

    //to fix

    /*public void shoot (PartialWeaponEffect partialWeaponEffect, FigureColour figureColour){
        for (Action action: partialWeaponEffect.getActions()){

        }
    }*/
}