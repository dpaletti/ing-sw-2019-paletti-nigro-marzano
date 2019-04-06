package it.polimi.se2019.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Newton extends PowerUp{
    @Override
    public Pair<List<Map<Figure, List<Action>>>, List<Ammo>> effect(Figure figure, List<Map<Figure, List<Action>>> storedMoves) {
        return null;
    }

    @Override
    protected Set<Figure> generateTargetSet(List<Map<Figure, List<Action>>> storedMoves) {
        return null;
    }
}
