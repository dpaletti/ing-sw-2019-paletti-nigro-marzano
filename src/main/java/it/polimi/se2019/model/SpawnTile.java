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
}