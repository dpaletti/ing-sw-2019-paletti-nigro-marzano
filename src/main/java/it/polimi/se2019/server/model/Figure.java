package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.utility.Point;

import java.util.Objects;

/**
 * This class defines the figures of the game. Each figure has a colour and a belongs to a player,
 * its tile is updated whenever the player moves the figure.
 * A figure implements all the basic movements and functionalities used by the player in its methods.
 */

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

    /**
     * This method causes damage to the target figure and adds the marks in case there are any.
     * @param target targeted figure to damage.
     */
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

    /**
     * This method marks another figure.
     * @param target targeted figure to mark.
     */
    public void mark (Figure target){
        int marksOfColour = 0;
        for(Tear m: target.player.getMarks()){ //
            if(m.getColour() == colour)
                marksOfColour++;
        }
        if(marksOfColour < 3)
            target.player.addMark(colour);
    }

    /**
     * Moves a player to their desired direction.
     * @param destination the desired destination.
     * @param distance the maximum distance the user can move.
     */
    public void run (Point destination, int distance){
            if(tile != null)
                tile.removeFigure(this);
            tile = player.getGameMap().getTile(destination);
            tile.addFigure(this);
    }

    /**
     * Grabs a grabbable.
     * @param grabbed the grabbable the user wishes to grab.
     */
    public void grab(String grabbed){
        tile.getGrabbables().get(0).grab(player, grabbed, player.getGame());
    }

    public void reload (Weapon weapon){
        weapon.setLoaded(true);
    }

    public void unload (Weapon weapon){
        weapon.setLoaded(false);
    }

    /**
     * Causes the defined damage to the chosen target.
     * @param partialWeaponEffect the effect of the weapon that the user wishes to cause.
     * @param figure the figure the user wishes to shoot.
     * @param game the class that communicates with the view.
     */

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
    public int hashCode() {
        return Objects.hash(colour);
    }

    @Override
    public String toString() {
        return "Figure{" +
                "colour=" + colour +
                '}';
    }
}