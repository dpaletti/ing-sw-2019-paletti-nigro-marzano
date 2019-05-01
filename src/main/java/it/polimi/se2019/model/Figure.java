package it.polimi.se2019.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Figure {
    private Tile tile;
    private FigureColour colour;
    private Player player;

    public FigureColour getColour() { return colour; }

    public Tile getTile() { return tile; }

    public void setColour(FigureColour colour) { this.colour = colour; }

    public void setTile(Tile tile) { this.tile = tile; }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

    private void damage(Figure target, Figure shooter){
        target.player.addTear(shooter.colour);
        target.player.updatePlayerDamage();
    }

    private void mark(Figure target, Figure shooter){
        int alreadyMaximumMarks=0;
        for(Tear marksCounter: target.player.getMarks()){
            if(marksCounter.getColour()==shooter.colour){
                alreadyMaximumMarks++;
            }
        }
        if(alreadyMaximumMarks<3) {
            target.player.addMark(shooter.colour);
        }
    }

    public void move(Direction direction){
        Point newPosition= new Point(tile.getPosition().getX(), tile.getPosition().getY());
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
            case TELEPORT:
                //TODO: create MVEvent to ask the user where he wants to be teleported
        }
        tile.setPosition(newPosition);
    }

    private void move(Figure target, Direction direction){
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
        }
    }

    public void grab(){
        if (tile.getTileType()==TileType.LOOTTILE) { //when on a Loot Tile, adds grabbed ammos to usable ammos making sure the number of ammos of a colour does not exceed 3
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
        else {  //se sono su una tile di spawn
            Weapon selectedWeapon= null;
            //l'utente sceglie un'arma da grabbare
            //TODO: creo classe evento mvevent devo essere in observable dell'evento e da lì mandare notify
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
                //l'utente sceglie un'arma da lasciare
            }
        }
    }

    private Set<Figure> generateTargetSet(GraphNode<Effect> node){return null; }

    public Map<Figure, List<Effect>> generateWeaponEffect(GraphNode<Effect> node){ return null;}

    public void reload(Weapon weapon){ //TODO: send "not enough ammo available" notif to user
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
            for (Ammo ammoToReload: weapon.getPrice()) {
                player.getUsableAmmo().remove(ammoToReload);
                player.getUnusableAmmo().add(ammoToReload);
            }
        }
    }

    private void damage (SpawnTile target){
        if (target.getTileType()==TileType.SPAWNTILE){
            target.addTear(colour);
        }

    }
}