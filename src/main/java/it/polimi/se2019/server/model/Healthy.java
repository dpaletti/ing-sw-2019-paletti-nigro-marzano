package it.polimi.se2019.server.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class defines a healthy player that was hit 0, 1 or 2 times. This player will only be able to use standard moves.
 * See {@link it.polimi.se2019.server.model.PlayerDamage}.
 */

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
