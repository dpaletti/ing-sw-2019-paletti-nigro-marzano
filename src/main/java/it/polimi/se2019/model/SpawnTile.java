package it.polimi.se2019.model;

import it.polimi.se2019.utility.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpawnTile extends Tile {

    public SpawnTile(GameMap gameMap, RoomColour colour, Map<Direction, Boolean> doors, Point position, List<Grabbable> grabbables) {
        super(gameMap, colour, doors, position, grabbables);
    }

    @Override
    public TileType getTileType() {
        return TileType.SPAWNTILE;
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