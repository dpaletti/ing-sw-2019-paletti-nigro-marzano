package it.polimi.se2019.server.model;

/**
 * A state pattern implements the lowering of the value of a certain player (points-wise) whenever they die.
 * This class lowers the maximum number of obtainable points after the first death to 8.
 * See {@link it.polimi.se2019.server.model.PlayerValue}.
 */


public class NoDeaths implements PlayerValue {

    @Override
    public int getMaxValue() {
        return 8;
    }

    @Override
    public PlayerValue getNextPlayerValue() {
        return new FirstDeath();
    }
}
