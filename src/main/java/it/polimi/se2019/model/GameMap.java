package it.polimi.se2019.model;

import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GameMap{
    private MapConfig config;
    private List<Tile> spawnTiles=new ArrayList<>();
    private List<Tile> lootTiles=new ArrayList<>();
    public GraphNode<Tile> graphMap;

    //TODO: delete enum MapConfig and JSON the pairing between map name and its halves
    public GameMap(MapConfig config){
        this.config= config;
        try {
            GameMap firstHalf=null;
            GameMap secondHalf=null;
            if (config.equals(MapConfig.MAP_SMALL) || config.equals(MapConfig.MAP_MEDIUM_RIGHT)) {
                firstHalf = (GameMap) JsonHandler.deserialize(new String(Files.readAllBytes(Paths.get("files/maps/Left1.json"))));
            } else {
                firstHalf = (GameMap) JsonHandler.deserialize(new String(Files.readAllBytes(Paths.get("files/maps/Left2.json"))));
            }
            if (config.equals(MapConfig.MAP_SMALL) || config.equals(MapConfig.MAP_MEDIUM_LEFT)) {
                secondHalf = (GameMap) JsonHandler.deserialize(new String(Files.readAllBytes(Paths.get("files/maps/Right2.json"))));
            } else {
                secondHalf = (GameMap) JsonHandler.deserialize(new String(Files.readAllBytes(Paths.get("files/maps/Right1.json"))));
            }
            this.spawnTiles.addAll(firstHalf.getSpawnTiles());
            this.spawnTiles.addAll(secondHalf.getSpawnTiles());
            this.lootTiles.addAll(firstHalf.getLootTiles());
            this.lootTiles.addAll(secondHalf.getLootTiles());
            graphMap = mapToGraph();
        }catch (IOException c){
            Log.severe("Map not found in given directory");
        }catch (NullPointerException e){
            Log.severe("Map not created: ");
        }catch (ClassNotFoundException e){
            Log.severe("Error in json file, type");
        }
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
        if (maximumDistance == -1){
            for (Tile t : getTiles())
                allowedMovements.add(t.position);
        }
        else {
            for (Tile t : getTiles()) {
                if (getDistance(currentTile.position, t.position) <= maximumDistance) {
                    allowedMovements.add(t.position);
                }
            }
        }

        return allowedMovements;
    }

    private int getDistance (Point startingPosition, Point endingPosition){
        return Math.abs(graphMap.getGraphNode(getTile(startingPosition)).getLayer() -
                graphMap.getGraphNode(getTile(endingPosition)).getLayer());
    }

    private GraphNode<Tile> mapToGraph (){
        GraphNode<Tile> root = new GraphNode<>(getTile(new Point(0, 0)), 0);
       getAdjacentTiles(root, 1);
       root.deleteCopies();
       return root;
    }

    private void getAdjacentTiles (GraphNode<Tile> root, int layer){        //layer is layer of its children
        for (Tile t: getTiles()){
            if (!root.isParent(t) &&
                    (Math.abs(root.getKey().position.getX() - t.position.getX()) == 1 ^
                    Math.abs(root.getKey().position.getY() - t.position.getY()) == 1) &&
                    root.getKey().colour.equals(t.colour) || hasDoor(root.getKey(), t))

                        root.addChild(new GraphNode<>(t, layer));
        }
        layer = layer + 1;
        for (GraphNode<Tile> t: root.getChildren())
            getAdjacentTiles(t, layer);
    }

    private boolean hasDoor (Tile root, Tile tile){
        if (root.position.getX() != tile.position.getX() && root.position.getY() != tile.position.getY())
            throw new IllegalArgumentException("tiles are not adjacent");
        return root.position.getX() == tile.position.getX() - 1 && root.doors.get(Direction.WEST) ||
                root.position.getX() == tile.position.getX() + 1 && root.doors.get(Direction.EAST) ||
                root.position.getY() == tile.position.getY() - 1 && root.doors.get(Direction.SOUTH) ||
                root.position.getY() == tile.position.getY() + 1 && root.doors.get(Direction.NORTH);
    }
}
