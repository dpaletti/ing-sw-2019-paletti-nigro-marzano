package it.polimi.se2019.model;

import java.util.Collections;
import java.util.List;

public class Healthy extends PlayerDamage {

    @Override
    List<List<Action>> moveSet(List<Action> elapsedMoves) { return super.moveSet(elapsedMoves); }

    @Override
    public Integer getMaximumHits() { return 2; }

}
