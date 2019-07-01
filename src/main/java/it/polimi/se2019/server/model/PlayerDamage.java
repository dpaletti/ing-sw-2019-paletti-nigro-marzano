package it.polimi.se2019.server.model;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerDamage {
    private Integer maximumHits;
    private boolean finalFrenzy;
    protected List<ArrayList<PartialCombo>> moves = new ArrayList<>();

    public Integer getMaximumHits() { return maximumHits; }

    public abstract List<ArrayList<PartialCombo>> getMoves();

    public abstract PlayerDamage findNextHealthState ();

    public abstract boolean isFinalFrenzy();

    protected abstract void addMoves ();

    public abstract PlayerDamage findPreviousHealthState ();
}
