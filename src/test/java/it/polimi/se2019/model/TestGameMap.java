package it.polimi.se2019.model;
import it.polimi.se2019.utility.Point;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestGameMap {
    GameMap map;

    @Test
    public void printMap(){
        map=new GameMap("Large");
        for(Tile tile: map.getLootTiles()){
            System.out.print(tile.getColour().toString()+'|'+tile.getPosition().getX()+','+tile.getPosition().getY()+System.lineSeparator());
        }
        for(Tile tile: map.getSpawnTiles()){
            System.out.print(tile.getColour().toString()+'|'+tile.getPosition().getX()+','+tile.getPosition().getY()+System.lineSeparator());
        }
    }

    @Test
    public void graphTest (){
        map = new GameMap("Small");
        System.out.println(map.graphMap);
    }

    @Test
    public void getTileTest(){
        map = new GameMap("Large");
        System.out.println(map.getTile(new Point(0, 2)) == map.getLootTiles().get(0));
    }

    @Test
    public void hasDoorTest (){
        map = new GameMap("Large");
        Tile root = map.getTile(new Point(0,0));
        Tile tile = map.getTile(new Point(0,1));
        System.out.println(map.hasDoor(root, tile));
    }

    @Test
    public void getDistanceTest (){
        map = new GameMap("Large");
        System.out.println(map.getDistance(new Point(0, 1), new Point(2, 2)));
    }

    @Test
    public void getAdjacentTiles (){
        map = new GameMap("Large");
        GraphNode<Tile> root = new GraphNode<>(map.getTile(new Point(0, 0)), 0);
        map.getAdjacentTiles(root, root, 1);
    }
}
