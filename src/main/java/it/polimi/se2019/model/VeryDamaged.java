package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VeryDamaged extends PlayerDamage {
    private Healthy nextHealthState;
    @Override
    public List<List<PartialCombo>> getMoves() {
        moves.add(Arrays.asList(PartialCombo.MOVE, PartialCombo.SHOOT));
        return moves;
    }

    @Override
    public Integer getMaximumHits() { return 10; }

    @Override
    public PlayerDamage findNextHealthState() {
        return nextHealthState;
    }

    @Override
    public boolean isFinalFrenzy() {
        return false;
    }
}
