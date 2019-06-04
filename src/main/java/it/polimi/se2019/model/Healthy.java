package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;

import java.util.List;

public class Healthy extends PlayerDamage {
    private SlightlyDamaged nextHealthState;

    @Override
    List<List<Action>> moveSet(List<Action> elapsedMoves) { return super.moveSet(elapsedMoves); }

    @Override
    public Integer getMaximumHits() { return 2; }

    public SlightlyDamaged getNextHealthState() {
        return nextHealthState;
    }

    public void setNextHealthState(SlightlyDamaged nextHealthState) {
        this.nextHealthState = nextHealthState;
    }

    @Override
    public PlayerDamage findNextHealthState() {
        return nextHealthState;
    }
}
