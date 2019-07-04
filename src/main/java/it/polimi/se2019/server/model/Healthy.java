package it.polimi.se2019.server.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Healthy extends PlayerDamage {
    private boolean added = false;

    @Override
    public List<ArrayList<PartialCombo>> getMoves() {
        if (!added)
            addMoves();
        return moves;
    }

    @Override
    public Integer getMaximumHits() { return 3; }

    @Override
    public PlayerDamage findNextHealthState() {
        return new SlightlyDamaged();
    }

    @Override
    public boolean isFinalFrenzy() {
        return false;
    }

    @Override
    protected void addMoves() {
        ArrayList<PartialCombo> combos = new ArrayList<>(Arrays.asList(PartialCombo.MOVE, PartialCombo.GRAB));
        moves.add(combos);
        combos = new ArrayList<>(Arrays.asList(PartialCombo.SHOOT));
        moves.add(combos);
        combos = new ArrayList<>(Arrays.asList(PartialCombo.MOVE,PartialCombo.MOVE, PartialCombo.MOVE));
        moves.add(combos);
        added = true;
    }

    @Override
    public PlayerDamage findPreviousHealthState() {
        throw new UnsupportedOperationException("There is no previous health state the player is Healthy");
    }
}
