package it.polimi.se2019.server.model;

/**
 * A state pattern implements the lowering of the value of a certain player (points-wise) whenever they die.
 */

public interface PlayerValue {
    int getMaxValue();
    PlayerValue getNextPlayerValue();
}
