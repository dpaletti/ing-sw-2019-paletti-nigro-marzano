package it.polimi.se2019.server.model;

public class ThirdDeath implements PlayerValue {

    @Override
    public int getMaxValue() {
        return 2;
    }

    @Override
    public PlayerValue getNextPlayerValue() {
        return new MoreDeaths();
    }
}
