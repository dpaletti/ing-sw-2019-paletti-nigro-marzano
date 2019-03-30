package it.polimi.se2019.model;

import it.polimi.se2019.model.Action;
import it.polimi.se2019.model.Pair;
import it.polimi.se2019.model.Weapon;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Flamethrower extends Weapon {
    @Override
    public Pair<List<Map<Figure, List<Action>>>, List<Ammo>> effect(Figure figure, List<Map<Figure, List<Action>>> storedMoves) {
        return null;
    }

    @Override
    public Set<Figure> generateTargetSet(List<Map<Figure, List<Action>>> storedMoves) {
        return null;
    }
}
