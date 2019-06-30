package it.polimi.se2019.server.model;

public class FirstDeath implements PlayerValue {

    @Override
    public int getMaxValue() {
        return 6;
    }

    @Override
    public PlayerValue getNextPlayerValue() {
        return new SecondDeath();
    }
}
