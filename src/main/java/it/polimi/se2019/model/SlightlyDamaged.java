package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;

import java.util.Collections;
import java.util.List;

public class SlightlyDamaged extends PlayerDamage {
    private VeryDamaged nextHealthState;

    @Override
    public List<List<Action>> moveSet(List<Action> elapsedMoves) {
        return Collections.emptyList();
    }

    @Override
    public Integer getMaximumHits() { return 5; }

    public VeryDamaged getNextHealthState() {
        return nextHealthState;
    }

    @Override
    public PlayerDamage findNextHealthState() {
        return nextHealthState;
    }
}
