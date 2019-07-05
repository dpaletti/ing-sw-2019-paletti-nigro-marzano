package it.polimi.se2019.server.model;

import java.util.List;

/**
 * This class extends the Tile class {@link it.polimi.se2019.server.model.Tile} and is grabbables are weapon that make up a weapon spot.
 */

public class SpawnTile extends Tile {

    public SpawnTile(Tile tile) {
        super(tile);
    }

    @Override
    public List<Grabbable> grab() {
        return grabbables;
    }

    @Override
    public void add(Grabbable grabbable) {
        if (grabbables.size()==3)
            throw new UnsupportedOperationException("weaponSpot is full, cannot add a fourth weapon to Spawn Tile");
        Weapon weapon= (Weapon) grabbable;
        grabbables.add(weapon);
    }

    @Override
    public void addAll(List<Grabbable> grabbablesToAdd) {
        if (grabbables.size()+grabbablesToAdd.size()>3)
            throw new UnsupportedOperationException("cannot add more than 3 weapons to weaponSpot on Spawn Tile");
        for (Grabbable g: grabbablesToAdd)
            add(g);
    }
}