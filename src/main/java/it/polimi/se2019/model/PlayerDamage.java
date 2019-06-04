package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;

import java.util.Collections;
import java.util.List;

public abstract class PlayerDamage {

    protected Integer maximumHits;

    public Integer getMaximumHits() { return maximumHits; }

    public void setMaximumHits(Integer maximumHits) {
        this.maximumHits = maximumHits;
    }

    List<List<Action>> moveSet (List<Action> elapsedMoves){return Collections.emptyList();};

    public abstract PlayerDamage findNextHealthState ();
}
