package it.polimi.se2019.model;

import java.awt.*;
import java.util.Map;
import java.util.Set;

public class GameMap {
    private static GameMap instance=null;
    private static MapConfig config;
    private static Map<Point, Tile> map;
    private static GameMode mode;
    private static Set<Tile> tiles;

    private GameMap(MapConfig config, GameMode mode, Map<Point, Tile> map){
        this.config= config;
        this.mode=mode;
        this.map=map;
    }

    public static GameMap getInstance(MapConfig config) {
        if (instance==null) {
            instance= new GameMap(config, mode, map);
        }
        return instance;
    }

    public GameMode getMode() {
        return mode;
    }

    public static Map<Point, Tile> getMap() {
        return map;
    }

    public MapConfig getConfig() {
        return config;
    }

    public static Set<Tile> getTiles() {
        return tiles;
    }

    public static Boolean checkBoundaries (Point position){
        return (map.containsKey(position));
    }
}
