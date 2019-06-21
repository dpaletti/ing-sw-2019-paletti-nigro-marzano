package it.polimi.se2019.model;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestGameMap {
    GameMap map;

    @Test
    public void printMap(){
        map=new GameMap(MapConfig.MAP_LARGE);
        for(Tile tile: map.getLootTiles()){
            System.out.print(tile.getColour().toString()+'|'+tile.getPosition().getX()+','+tile.getPosition().getY()+System.lineSeparator());
        }
        for(Tile tile: map.getSpawnTiles()){
            System.out.print(tile.getColour().toString()+'|'+tile.getPosition().getX()+','+tile.getPosition().getY()+System.lineSeparator());
        }
    }
}
