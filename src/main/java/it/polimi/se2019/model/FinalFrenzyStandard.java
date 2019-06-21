package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FinalFrenzyStandard extends PlayerDamage {

    @Override
    public List<List<PartialCombo>> getMoves() {
        moves.clear();
        moves.add(Arrays.asList(PartialCombo.MOVE, PartialCombo.MOVE, PartialCombo.RELOAD, PartialCombo.SHOOT));
        moves.add(Arrays.asList(PartialCombo.MOVE, PartialCombo.MOVE, PartialCombo.MOVE, PartialCombo.GRAB));

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
}
