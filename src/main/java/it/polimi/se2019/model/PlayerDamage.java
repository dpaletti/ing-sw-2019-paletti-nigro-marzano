package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;
import it.polimi.se2019.utility.ActionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PlayerDamage {
    private Integer maximumHits;
    private boolean finalFrenzy;
    protected List<List<PartialCombo>> moves = new ArrayList<>();

    public Integer getMaximumHits() { return maximumHits; }

    public abstract List<List<PartialCombo>> getMoves();

    public abstract PlayerDamage findNextHealthState ();

    public abstract boolean isFinalFrenzy();
}
