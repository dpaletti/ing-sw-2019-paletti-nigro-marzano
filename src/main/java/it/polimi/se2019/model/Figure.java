package it.polimi.se2019.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Figure {
    private Point position;
    private FigureColour colour;
    private Player player;

    public FigureColour getColour() {
        return colour;
    }

    public Point getPosition() {
        return position;
    }

    public void setColour(FigureColour colour) {
        this.colour = colour;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

    private void damage(Figure target){}

    private void mark(Figure target){}

    public void move(Direction direction){}

    private void move(Figure target, Direction direction){}

    public void grab(){}

    private Set<Figure> generateTargetSet(GraphNode<Effect> node){return null;}

    public Map<Figure, List<Effect>> generateWeaponEffect(GraphNode<Effect> node){return null;}

    public void reload(Weapon weapon){}

    private void damage (SpawnTile target){}
}