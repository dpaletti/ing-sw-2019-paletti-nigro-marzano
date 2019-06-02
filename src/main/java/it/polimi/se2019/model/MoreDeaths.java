package it.polimi.se2019.model;

public class MoreDeaths implements PlayerValue {

    @Override
    public int getMaxValue() {
        return 1;
    }

    @Override
    public PlayerValue getNextPlayerValue() {
        return new MoreDeaths();
    }
}
