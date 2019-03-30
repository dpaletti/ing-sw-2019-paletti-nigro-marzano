package it.polimi.se2019.model;

import java.util.List;

public abstract class PowerUp {
    private AmmoColour colour;

    public abstract
    Pair<List<Map<Figure, List<Action>>>, List<Ammo>>
    effect(Figure figure, List<Map<Figure, List<Action>>> storedMoves);
    protected abstract
    Set<Figure>
    generateTargetSet(List<Map<Figure, List<Action>>> storedMoves);

    public AmmoColour getColour() {
        return colour;
    }

    public void setColour(AmmoColour colour) {
        this.colour = colour;
    }
}
