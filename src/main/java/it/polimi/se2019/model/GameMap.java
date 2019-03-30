package it.polimi.se2019.model;

import java.awt.*;
import java.util.Map;

public class GameMap {
    private static GameMap instance;
    private MapConfig config;
    private Map<Point, Tile> map;
    private GameMode mode;
    private Boolean finalFrenzy;

    private GameMap(MapConfig config, GameMode mode, Boolean finalFrenzy){};

    public static GameMap getInstance() {
        return instance;
    }

    public Boolean getFinalFrenzy() {
        return finalFrenzy;
    }

    public GameMode getMode() {
        return mode;
    }

    public Map<Point, Tile> getMap() {
        return map;
    }

    public MapConfig getConfig() {
        return config;
    }

    public void setConfig(MapConfig config) {
        this.config = config;
    }

    public void setFinalFrenzy(Boolean finalFrenzy) {
        this.finalFrenzy = finalFrenzy;
    }

    public static void setInstance(GameMap instance) {
        GameMap.instance = instance;
    }

    public void setMap(Map<Point, Tile> map) {
        this.map = map;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

}
