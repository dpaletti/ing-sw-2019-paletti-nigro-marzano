package it.polimi.se2019.model;

import it.polimi.se2019.utility.Point;

import java.util.Map;
import java.util.Set;

public class GameMap {
    //TODO create one JSON for each half of the map and bind them together when constructing
    private MapConfig config;
    private Map<it.polimi.se2019.utility.Point, Tile> map;
    private GameMode mode;
    private Set<Tile> tiles;

    public GameMap(MapConfig config, GameMode mode, Map<it.polimi.se2019.utility.Point, Tile> map){
        this.config= config;
        this.mode=mode;
        this.map=map;
    }

    public GameMode getMode() {
        return mode;
    }

    public Map<it.polimi.se2019.utility.Point, Tile> getMap() {
        return map;
    }

    public MapConfig getConfig() {
        return config;
    }

    public Set<Tile> getTiles() {
        return tiles;
    }

    public Boolean checkBoundaries (Point position){
        return (map.containsKey(position));
    }
}
