package it.polimi.se2019.model;

import java.util.Collections;
import java.util.List;

public abstract class PlayerDamage {

    private Integer maximumHits;

    public Integer getMaximumHits() { return maximumHits; }

    List<List<Action>> moveSet (List<Action> elapsedMoves){return Collections.emptyList();};
}
