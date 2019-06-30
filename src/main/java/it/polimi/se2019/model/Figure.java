package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;
import it.polimi.se2019.utility.Point;

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
        if (target.getPlayer().getHp().size()==12)
            return;
        target.player.addTear(colour);
        for (Tear tear: target.player.getMarks()){
            if (tear.getColour().equals(colour)){
                target.player.addTear(colour);
                target.player.removeMark(colour);
            }
        }
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
            if(tile != null)
                tile.removeFigure(this);
            tile = player.getGameMap().getTile(destination);
            tile.addFigure(this);
    }

    public void grab(String grabbed){
        tile.getGrabbables().get(0).grab(player, grabbed, player.getGame());
    }

    public void reload (Weapon weapon){
        weapon.setLoaded(true);
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

    @Override
    public String toString() {
        return "Figure{" +
                "colour=" + colour +
                '}';
    }
}