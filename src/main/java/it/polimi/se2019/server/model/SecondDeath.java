package it.polimi.se2019.server.model;

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
