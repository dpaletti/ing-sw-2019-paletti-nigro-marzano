package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.utility.Point;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestTile {
    GameMap map= new GameMap("Small");
    Tile tile= new Tile(map.getTile(new Point(0,1)));
    Game model= new Game();

    @Test (expected = UnsupportedOperationException.class)
    public void testGrab(){
        tile.grab();
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testAdd(){
        tile.add((Weapon)model.getWeaponDeck().draw());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void addAll(){
        List<Grabbable> weapons= new ArrayList<>();
        weapons.add((Weapon)model.getWeaponDeck().draw());
        weapons.add((Weapon)model.getWeaponDeck().draw());
        tile.addAll(weapons);
    }

    @Test
    public void testHit(){
        Player player= new Player(new Figure(FigureColour.GREEN),model);
    }
}
