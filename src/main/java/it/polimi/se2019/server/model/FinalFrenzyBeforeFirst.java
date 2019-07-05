package it.polimi.se2019.server.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class implements the state pattern. As during final frenzy game mode players can use a different set of moves and combos
 * based on their position in the current turn and on the position of the last player to die, this set of actions is assigned
 * to players who have their turn before the first player but after the last dead one. See {@link it.polimi.se2019.server.model.PlayerDamage}.
 */

public class FinalFrenzyBeforeFirst extends PlayerDamage {
    private boolean added = false;
    private boolean isBefore=true;

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

    @Override
    public PlayerDamage findPreviousHealthState() {
        throw new UnsupportedOperationException("The player is in FinalFrenzyBeforeState there is no previous health state");
    }

    public boolean isBefore() {
        return isBefore;
    }
}
