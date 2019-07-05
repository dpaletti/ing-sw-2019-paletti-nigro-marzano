package it.polimi.se2019.server.model;

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
