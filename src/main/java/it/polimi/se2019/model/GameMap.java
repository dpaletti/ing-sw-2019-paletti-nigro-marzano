package it.polimi.se2019.model;

import java.awt.*;
import java.util.Map;
import java.util.Set;

public class GameMap {
    private static GameMap instance=null;
    private static MapConfig config;
    private static Map<Point, Tile> map;
    private static GameMode mode;
    private static Boolean finalFrenzy;
    private static Set<Tile> tiles;

    private GameMap(MapConfig config, GameMode mode, Boolean finalFrenzy, Map<Point, Tile> map){
        this.config= config;
        this.mode=mode;
        this.finalFrenzy=finalFrenzy;
        this.map=map;
    }

    public static GameMap getInstance(MapConfig config) {
        if (instance==null) {
            instance= new GameMap(config, mode, finalFrenzy, map);
        }
        return instance;
    }

    public Boolean getFinalFrenzy() {
        return finalFrenzy;
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
}
