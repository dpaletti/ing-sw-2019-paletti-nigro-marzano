package it.polimi.se2019.model;

import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Point;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GameMap{
    private MapConfig config;
    private List<Tile> spawnTiles=new ArrayList<>();
    private List<Tile> lootTiles=new ArrayList<>();
    public GraphNode<Tile> graphMap;
    private Point root;

    public GameMap(String configName){
        try {
            this.config= (MapConfig)JsonHandler.deserialize(readFile("files/mapConfigs/"+configName+".json"));
            GameMap leftHalf = (GameMap) JsonHandler.deserialize(readFile("files/maps/"+config.getLeftHalf()+".json"));
            GameMap rightHalf= (GameMap) JsonHandler.deserialize(readFile("files/maps/"+config.getRightHalf()+".json"));
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

    private String readFile (String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        return new String(data, StandardCharsets.UTF_8);
    }

    public MapConfig getConfig() {
        return config;
    }

    public Set<Tile> getTiles() {
        Set<Tile> allTiles= new HashSet<>(spawnTiles);
        allTiles.addAll(lootTiles);
        return allTiles;
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
        return generateGraph(startingPosition).getGraphNode(getTile(endingPosition)).getLayer();
    }

    private GraphNode<Tile> generateGraph (Point root){
        GraphNode<Tile> mapRoot = new GraphNode<>(getTile(root), 0);
        getAdjacentTiles(mapRoot, mapRoot, 1);
        mapRoot.deleteCopies();
        return mapRoot;
    }

    private GraphNode<Tile> mapToGraph (){
        GraphNode<Tile> mapRoot = new GraphNode<>(getTile(root), 0);
       getAdjacentTiles(mapRoot, mapRoot, 1);
       mapRoot.deleteCopies();
       return mapRoot;
    }

     private void getAdjacentTiles (GraphNode<Tile> root, GraphNode<Tile> localRoot, int layer){        //layer is layer of its children
        GraphNode<Tile> child;
        for (Tile t: getTiles()){
            if (!localRoot.isParent(t)){
                if (Math.abs(localRoot.getKey().position.getX() - t.position.getX()) == 1 ^
                        Math.abs(localRoot.getKey().position.getY() - t.position.getY()) == 1){
                        if (localRoot.getKey().colour.name().equals(t.colour.name()) || hasDoor(localRoot.getKey(), t)) {
                            child = new GraphNode<>(t, layer);
                            if (!root.isIn(t) || root.getGraphNode(t).getLayer() > layer) {
                                if (root.isIn(t) && root.getGraphNode(t).getLayer() > layer){
                                    for (GraphNode<Tile> g : root.getGraphNode(t).getParents())
                                        g.removeChild(root.getGraphNode(t));
                                }
                                localRoot.addChild(child);
                                child.addParent(localRoot);
                            }
                        }
                    }
            }
        }
        layer = layer + 1;
        for (GraphNode<Tile> t: localRoot.getChildren())
            getAdjacentTiles(root, t, layer);
    }

    private boolean hasDoor (Tile root, Tile tile){
        if (root.position.getX() != tile.position.getX() && root.position.getY() != tile.position.getY())
            return false;
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

    public Map<Point,String> getMappedSpawnPoints(){
        Map<Point, String> spawnPoints= new HashMap<>();
        for(Tile t: spawnTiles)
            spawnPoints.put(t.getPosition(), t.getColour().toString());
        return spawnPoints;
    }

}
