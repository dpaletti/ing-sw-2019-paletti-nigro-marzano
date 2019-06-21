package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;
import it.polimi.se2019.utility.ActionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Healthy extends PlayerDamage {
    private SlightlyDamaged nextHealthState;

    @Override
    public List<List<PartialCombo>> getMoves() {
        moves.add(Arrays.asList(PartialCombo.MOVE, PartialCombo.GRAB));
        moves.add(Arrays.asList(PartialCombo.SHOOT));
        moves.add(Arrays.asList(PartialCombo.MOVE,PartialCombo.MOVE, PartialCombo.MOVE));
        return moves;
    }

    @Override
    public Integer getMaximumHits() { return 2; }

    public SlightlyDamaged getNextHealthState() {
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
