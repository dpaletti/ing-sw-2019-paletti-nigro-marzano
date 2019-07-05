package it.polimi.se2019.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a state pattern thanks to which it is possible to add new moves to a damaged player.
 * See its subclasses: {@link it.polimi.se2019.server.model.FinalFrenzyStandard}, {@link it.polimi.se2019.server.model.FinalFrenzyBeforeFirst},
 * {@link it.polimi.se2019.server.model.Healthy}, {@link it.polimi.se2019.server.model.SlightlyDamaged}, {@link it.polimi.se2019.server.model.VeryDamaged}.
 */

public abstract class PlayerDamage {
    private Integer maximumHits;
    private boolean finalFrenzy;
    private boolean isBefore=true;
    protected List<ArrayList<PartialCombo>> moves = new ArrayList<>();

    public Integer getMaximumHits() { return maximumHits; }

    public abstract List<ArrayList<PartialCombo>> getMoves();

    public abstract PlayerDamage findNextHealthState ();

    public abstract boolean isFinalFrenzy();

    protected abstract void addMoves ();

    public abstract PlayerDamage findPreviousHealthState ();

    public boolean isBefore(){throw new UnsupportedOperationException("");}
}
