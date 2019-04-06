package it.polimi.se2019.model;

import java.awt.*;
import java.util.Set;

public class Figure {
    private Point position;
    private FigureColour colour;

    public Set<Figure> getVisibilitySet();
    public void damage(Set<Figure> targets);
    public void mark (Set<Figure> targets);
    public void move (Set<Figure> targets, Direction direction);
    public void move (Direction direction);
    public void dominationDamage (Set<SpawnTile> targets);

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
}
