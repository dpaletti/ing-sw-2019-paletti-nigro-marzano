package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SlightlyDamaged extends PlayerDamage {
    private VeryDamaged nextHealthState;

    @Override
    public List<List<PartialCombo>> getMoves() {
        moves.add(Arrays.asList(PartialCombo.MOVE, PartialCombo.MOVE, PartialCombo.GRAB));
        return moves;
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

    @Override
    public boolean isFinalFrenzy() {
        return false;
    }
}
