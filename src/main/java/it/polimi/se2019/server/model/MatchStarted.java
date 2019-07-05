package it.polimi.se2019.server.model;

/**
 * A state pattern implements the lowering of the value of a certain player (points-wise) whenever they die.
 * See {@link it.polimi.se2019.server.model.PlayerValue}.
 */

public class MatchStarted implements PlayerValue {

    @Override
    public int getMaxValue() {
        return 0;
    }

    @Override
    public PlayerValue getNextPlayerValue() {
        return new NoDeaths();
    }


}
