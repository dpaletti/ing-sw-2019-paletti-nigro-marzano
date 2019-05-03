package it.polimi.se2019.model;

import java.util.Collections;
import java.util.List;

public class SlightlyDamaged extends PlayerDamage {
    @Override
    public List<List<Action>> moveSet(List<Action> elapsedMoves) {
        return Collections.emptyList();
    }

    @Override
    public Integer getMaximumHits() { return 5; }
}
