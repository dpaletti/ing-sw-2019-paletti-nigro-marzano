package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;
import it.polimi.se2019.utility.Point;

import java.util.List;
import java.util.Set;

public class Figure {
    private Tile tile;
    private FigureColour colour;
    private Player player;

    public Figure (FigureColour figureColour){
        this.colour= figureColour;
    }

    public Point getPosition() {
        return new Point(tile.getPosition());
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
    }       //needed as figure has reference to player and vice versa

    public void damage (Figure target){
        target.player.addTear(colour);
        for (Tear tear: target.player.getMarks()){
            if (tear.getColour().equals(colour)){
                target.player.addTear(colour);
                target.player.removeMark(colour);
            }
        }
        target.player.updatePlayerDamage();
    }

    public void mark (Figure target){
        int marksOfColour = 0;
        for(Tear m: target.player.getMarks()){ //
            if(m.getColour() == colour)
                marksOfColour++;
        }
        if(marksOfColour < 3)
            target.player.addMark(colour);
    }

    public void run (Point destination, int distance){
        if (player.getGameMap().getAllowedMovements(tile, distance).contains(destination)){
            tile.removeFigure(this);
            tile = player.getGameMap().getTile(destination);
            tile.addFigure(this);
        }
    }

    public void grab(String grabbed){
        for (Grabbable g : tile.getGrabbables())
            g.grab(player, grabbed);
    }

    public void reload (Weapon weapon){
        weapon.setLoaded(true);
        player.useAmmos(weapon.getReloadPrice());
    }

    public void unload (Weapon weapon){
        weapon.setLoaded(false);
    }

    public void shoot (PartialWeaponEffect partialWeaponEffect, Figure figure, Game game){
       for(Action a : partialWeaponEffect.getActions())
           a.getActionType().apply(figure.player, player, a, game);
    }

    public void spawn (Point spawnPosition){
        tile = player.getGameMap().getTile(spawnPosition);
        tile.addFigure(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Figure figure = (Figure) o;
        return colour == figure.colour;
    }

}