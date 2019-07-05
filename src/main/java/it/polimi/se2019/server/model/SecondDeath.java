package it.polimi.se2019.server.model;

/**
 * A state pattern implements the lowering of the value of a certain player (points-wise) whenever they die.
 * This class lowers the maximum number of obtainable points after the first death to 4.
 * See {@link it.polimi.se2019.server.model.PlayerValue}.
 */

public class SecondDeath implements PlayerValue {

    @Override
    public int getMaxValue() {
        return 4;
    }

    @Override
    public PlayerValue getNextPlayerValue() {
        return new ThirdDeath();
    }
}
