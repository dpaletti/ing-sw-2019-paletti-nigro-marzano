package it.polimi.se2019.model;

import java.util.List;

public class Teleporter extends PowerUp {
    @Override
    protected Set<Figure> generateTargetSet(List<Map<Figure, List<Action>>> storedMoves) {
        return null;
    }

    @Override
    public Pair<List<Map<Figure, List<Action>>>, List<Ammo>> effect(Figure figure, List<Map<Figure, List<Action>>> storedMoves) {
        return null;
    }
}
