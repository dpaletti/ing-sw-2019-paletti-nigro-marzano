package it.polimi.se2019.model;

import it.polimi.se2019.utility.PartialCombo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlightlyDamaged extends PlayerDamage {
    private VeryDamaged nextHealthState;
    private boolean added = false;

    @Override
    public List<ArrayList<PartialCombo>> getMoves() {
        if (!added)
            addMoves();
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

    @Override
    protected void addMoves() {
        ArrayList<PartialCombo> combos = new ArrayList<>(Arrays.asList(PartialCombo.MOVE, PartialCombo.MOVE, PartialCombo.GRAB));
        moves.add(combos);
        added = true;
    }
}
