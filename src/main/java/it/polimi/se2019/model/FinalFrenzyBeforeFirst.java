package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;

import java.util.Collections;
import java.util.List;

public class FinalFrenzyBeforeFirst extends PlayerDamage {
    @Override
    public List<List<Action>> moveSet(List<Action> elapsedMoves) {
        return Collections.emptyList();
    }

    @Override
    public PlayerDamage findNextHealthState() {
        return this;
    }
}
