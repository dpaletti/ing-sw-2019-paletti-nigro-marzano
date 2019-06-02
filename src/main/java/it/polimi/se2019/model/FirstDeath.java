package it.polimi.se2019.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
