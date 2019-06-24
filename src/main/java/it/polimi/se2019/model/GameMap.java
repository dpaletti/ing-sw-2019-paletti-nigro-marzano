package it.polimi.se2019.model;

import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class GameMap{
    private MapConfig config;
    private List<Tile> spawnTiles=new ArrayList<>();
    private List<Tile> lootTiles=new ArrayList<>();
    public GraphNode<Tile> graphMap;
    private Point root;

    //TODO: delete enum MapConfig and JSON the pairing between map name and its halves
    public GameMap(String configName){
        try {
            this.config= (MapConfig)JsonHandler.deserialize(new String(Files.readAllBytes(Paths.get("files/mapConfigs/"+configName+".json"))));
            GameMap leftHalf = (GameMap) JsonHandler.deserialize(new String(Files.readAllBytes(Paths.get("files/maps/"+config.getLeftHalf()+".json"))));
            GameMap rightHalf= (GameMap) JsonHandler.deserialize(new String(Files.readAllBytes(Paths.get("files/maps/"+config.getRightHalf()+".json"))));
            this.spawnTiles.addAll(castTileSpawn(leftHalf.getSpawnTiles()));
            this.spawnTiles.addAll(castTileSpawn(rightHalf.getSpawnTiles()));
            this.lootTiles.addAll(castTileLoot(leftHalf.getLootTiles()));
            this.lootTiles.addAll(castTileLoot(rightHalf.getLootTiles()));
            this.root=leftHalf.root;
            graphMap=mapToGraph();
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

     int getDistance (Point startingPosition, Point endingPosition){
        return Math.abs(graphMap.getGraphNode(getTile(startingPosition)).getLayer() -
                graphMap.getGraphNode(getTile(endingPosition)).getLayer());
    }

    private GraphNode<Tile> mapToGraph (){
        GraphNode<Tile> mapRoot = new GraphNode<>(getTile(root), 0);
       getAdjacentTiles(mapRoot, mapRoot, 1);
       mapRoot.deleteCopies();
       return mapRoot;
    }

     void getAdjacentTiles (GraphNode<Tile> root, GraphNode<Tile> localRoot, int layer){        //layer is layer of its children
        GraphNode<Tile> child;
        for (Tile t: getTiles()){
            if (!localRoot.isParent(t)){
                if (Math.abs(localRoot.getKey().position.getX() - t.position.getX()) == 1 ^
                        Math.abs(localRoot.getKey().position.getY() - t.position.getY()) == 1){
                        if (localRoot.getKey().colour.name().equals(t.colour.name()) || hasDoor(localRoot.getKey(), t)) {
                            child = new GraphNode<>(t, layer);
                            if (!root.isIn(t) || root.getGraphNode(t).getLayer() > layer) {
                                if (root.isIn(t) && root.getGraphNode(t).getLayer() > layer){
                                    Log.fine("deleting copy:" + t.position.getX() + t.position.getY());
                                    for (GraphNode<Tile> g : root.getGraphNode(t).getParents())
                                        g.removeChild(root.getGraphNode(t));
                                }
                                localRoot.addChild(child);
                                child.addParent(localRoot);
                            }
                            System.out.println("tile with position " + localRoot.getKey().position.getX() + localRoot.getKey().position.getY() + " has adjacent " + t.position.getX() + t.position.getY());
                        }
                    }
            }
        }
        layer = layer + 1;
        //root.deleteCopies();
        for (GraphNode<Tile> t: localRoot.getChildren())
            getAdjacentTiles(root, t, layer);
    }

    boolean hasDoor (Tile root, Tile tile){
        if (root.position.getX() != tile.position.getX() && root.position.getY() != tile.position.getY())
            return false;
            //throw new IllegalArgumentException("tiles are not adjacent, root:" + root.position.getX() + root.position.getY() + "tile:" + tile.position.getX() + tile.position.getY());
        return (root.position.getX() - 1 == tile.position.getX() && root.doors.get(Direction.WEST)) ||
                (root.position.getX() + 1 == tile.position.getX() && root.doors.get(Direction.EAST)) ||
                (root.position.getY() - 1 == tile.position.getY() && root.doors.get(Direction.SOUTH)) ||
                (root.position.getY() + 1 == tile.position.getY() && root.doors.get(Direction.NORTH));
    }

    private List<SpawnTile> castTileSpawn(List<Tile> spawnTiles){
        List<SpawnTile> castedTiles= new ArrayList<>();
        for (Tile t: spawnTiles)
            castedTiles.add(new SpawnTile(t));
        return castedTiles;
    }

    private List<LootTile> castTileLoot(List<Tile> lootTiles){
        List<LootTile> castedTiles= new ArrayList<>();
        for (Tile t: lootTiles)
            castedTiles.add(new LootTile(t));
        return castedTiles;
    }

}
