package it.polimi.se2019.model;

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

    private void damage(Set<Figure> targets){}

    private void mark(Set<Figure> targets){}

    public void move(Direction direction){}

    private void move(Set<Figure> targets, Direction direction){}

    private void dominationDamage(Set<SpawnTile> targets){}

    public void grab(){}

    private Set<Figure> generateTargetSet(Set<Figure> figures){return null;}

    private Set<Figure> getVisibleTargetSet(Set<Figure> figures){return null;}

    private Set<Figure> getDifferentTargetSet(Set<Figure> attackedFigures){return null;}

    private Set<Figure> getZoneTargets(Set<Tile> tiles){return null;}

    private Set<Figure> getRadiusBetweenTargets(Integer innerRadius, Integer outerRadius){return null;}

    public void reload(Weapon weapon){}
}