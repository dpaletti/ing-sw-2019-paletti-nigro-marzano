package it.polimi.se2019.server.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FinalFrenzyStandard extends PlayerDamage {
    private boolean added = false;
    private boolean isBefore=false;

    @Override
    public List<ArrayList<PartialCombo>> getMoves() {
       if (!added)
           addMoves();
        return moves;
    }

    @Override
    public PlayerDamage findNextHealthState() {
        return this;
    }

    @Override
    public boolean isFinalFrenzy() {
        return true;
    }

    @Override
    protected void addMoves() {
        moves.clear();
        ArrayList<PartialCombo> combos = new ArrayList<>
                (Arrays.asList(PartialCombo.MOVE, PartialCombo.MOVE, PartialCombo.RELOAD, PartialCombo.SHOOT));
        moves.add(combos);
        combos = new ArrayList<>(Arrays.asList(PartialCombo.MOVE, PartialCombo.MOVE, PartialCombo.MOVE, PartialCombo.GRAB));
        moves.add(combos);
        added = true;
    }

    @Override
    public PlayerDamage findPreviousHealthState() {
        throw new UnsupportedOperationException("The player is in FinalFrenzyStandard there is no previous health state");
    }

    public boolean isBefore() {
        return isBefore;
    }
}
