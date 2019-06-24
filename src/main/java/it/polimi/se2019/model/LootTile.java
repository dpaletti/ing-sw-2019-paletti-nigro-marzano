package it.polimi.se2019.model;

import it.polimi.se2019.utility.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LootTile extends Tile{

    public LootTile(Tile tile) {
        super(tile);
    }

    @Override
    public List<Grabbable> grab() {
        return grabbables;
    }

    @Override
    public void add(Grabbable grabbable) {
        if(!grabbables.isEmpty())
            throw new UnsupportedOperationException("loot card already placed on Loot Tile, cannot add a new one");
        LootCard loot = (LootCard) grabbable;
        grabbables.add(loot);
    }

    @Override
    public void addAll(List<Grabbable> grabbablesToAdd) {
        throw new UnsupportedOperationException("cannot add more than one loot card to a Loot Tile");
    }
}


