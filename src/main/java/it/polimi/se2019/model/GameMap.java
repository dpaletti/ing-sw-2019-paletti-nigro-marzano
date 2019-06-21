package it.polimi.se2019.model;

import it.polimi.se2019.utility.Point;

import java.util.*;

public class GameMap {
    //TODO create one JSON for each half of the map and bind them together when constructing
    private MapConfig config;
    private GameMode mode;
    private List<Tile> spawnTiles;
    private List<Tile> lootTiles;

    public GameMap(MapConfig config, GameMode mode){
        this.config= config;
        this.mode=mode;
    }

    public GameMode getMode() {
        return mode;
    }

    public MapConfig getConfig() {
        return config;
    }

    public Set<Tile> getTiles() {
        Set<Tile> allTiles= new HashSet<>(spawnTiles);
        allTiles.addAll(lootTiles);
        return allTiles;
    }

    public boolean checkBoundaries (Point position){
        return (getTiles().contains(getTile(position)));
    }

    public Set<Tile> getRoom (RoomColour roomColour){
        Set<Tile> room= new HashSet<>();
        for (Tile t: getTiles()){
            if (t.getColour().equals(roomColour))
                room.add(t);
        }
        return room;
    }

    public Tile getTile (Point position){
        for (Tile t: getTiles()){
            if (position.equals(t.position))
                return t;
        }
        throw new NullPointerException("tile with given position not found");
    }

    public List<Tile> getLootTiles() {
        return new ArrayList<>(lootTiles);
    }

    public List<Tile> getSpawnTiles() {
        return new ArrayList<>(spawnTiles);
    }

    public List<Point> getPoints (){
        List<Point> allPoints = new ArrayList<>();
        for (Tile t: getTiles())
            allPoints.add(t.position);
        return allPoints;
    }

    public List<Point> getAllowedMovements (Tile currentTile, int maximumDistance){
        List<Point> allowedMovements= new ArrayList<>();

        for (Tile t: getTiles()){
            if (Math.abs(currentTile.position.getX()-t.position.getX())+
                    Math.abs(currentTile.position.getY()-t.position.getY())
                    ==maximumDistance) {
                allowedMovements.add(t.position);
            }
        }

        return allowedMovements;
    }
}
