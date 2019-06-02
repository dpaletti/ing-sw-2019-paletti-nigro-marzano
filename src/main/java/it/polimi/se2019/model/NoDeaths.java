package it.polimi.se2019.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
