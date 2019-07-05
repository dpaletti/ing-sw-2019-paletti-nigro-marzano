package it.polimi.se2019.server.model;

import java.util.List;

/**
 * This class defines a loot tile, its grabbable is a loot card.
 */

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


