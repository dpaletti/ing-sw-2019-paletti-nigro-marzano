package it.polimi.se2019.server.model;

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
