package it.polimi.se2019.model;

import it.polimi.se2019.utility.PartialCombo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FinalFrenzyBeforeFirst extends PlayerDamage {
    private boolean added = false;

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
        ArrayList<PartialCombo> combos = new ArrayList<>(Arrays.asList(PartialCombo.MOVE, PartialCombo.RELOAD, PartialCombo.SHOOT));
        moves.add(combos);
        combos = new ArrayList<>(Arrays.asList(PartialCombo.MOVE, PartialCombo.MOVE, PartialCombo.MOVE, PartialCombo.MOVE));
        moves.add(combos);
        combos = new ArrayList<>(Arrays.asList(PartialCombo.MOVE, PartialCombo.MOVE, PartialCombo.GRAB));
        moves.add(combos);
        added = true;
    }
}
