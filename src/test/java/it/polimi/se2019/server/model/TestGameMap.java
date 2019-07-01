package it.polimi.se2019.server.model;
import it.polimi.se2019.commons.utility.Point;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;


public class TestGameMap {
    GameMap gameMap= new GameMap("Small");

    @Test
    public void testGetRoom(){
        Set<Tile> redRoom= gameMap.getRoom(RoomColour.RED);
        Tile redSpawnPoint= gameMap.getTile(new Point(0,1));
        assertTrue(redRoom.contains(redSpawnPoint));
    }

    @Test(expected = NullPointerException.class)
    public void getTileException(){
        gameMap.getTile(new Point(0,0));
    }

    @Test
    public void testGetPoints(){
        List<Point> points= gameMap.getPoints();
       for (int y=0;y<3;y++){
           for (int x=0;x<4;x++){
               if ((x!=0 && y!=0) &&(x!=3 && y!=2))
                   assertTrue(points.contains(new Point(x,y)));
           }
        }
    }

    @Test
    public void testGetAllowedMovements(){
        Tile tile= gameMap.getTile(new Point(0,1));
        List<Point> allowed= gameMap.getAllowedMovements(tile,3);
        for (int y=0;y<3;y++){
            for (int x=0;x<3;x++){
               if (y==2 && (y==1 && x==1))
                   assertTrue(allowed.contains(new Point(x,y)));
            }
        }
    }




}
