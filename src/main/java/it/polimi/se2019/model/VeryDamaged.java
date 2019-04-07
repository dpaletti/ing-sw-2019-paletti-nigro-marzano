package it.polimi.se2019.model;

import java.util.Collections;
import java.util.List;

public class VeryDamaged implements PlayerDamage {
    @Override
    public List<List<Action>> moveSet(List<Action> elapsedMoves) {
        return Collections.emptyList();
    }
}