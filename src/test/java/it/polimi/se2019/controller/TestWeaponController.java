package it.polimi.se2019.controller;
import it.polimi.se2019.model.*;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Point;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;
public class TestWeaponController {

    private Game game = new Game();
    private List<Player> players = new ArrayList<>();
    private WeaponController weaponController = new WeaponController(game);
    private Player magenta= new Player(new Figure(FigureColour.MAGENTA), game);
    private Player blue= new Player(new Figure(FigureColour.BLUE), game);
    private Player yellow= new Player(new Figure(FigureColour.YELLOW), game);
    private Player grey= new Player(new Figure(FigureColour.GREY), game);
    private Player green= new Player(new Figure(FigureColour.GREEN), game);

    @Before
    public void setup (){
        game.setGameMap(new GameMap("Large"));
        magenta.getFigure().spawn(new Point(0, 0));
        green.getFigure().spawn(new Point(1, 0));
        blue.getFigure().spawn(new Point(0, 0));
        yellow.getFigure().spawn(new Point(2,2));
        grey.getFigure().spawn(new Point(1, 1));

        players.add(magenta);
        players.add(blue);
        players.add(grey);
        players.add(green);
        players.add(yellow);
        game.setPlayers(players);
    }

    @Test
    public void testAreaSelection(){
        List<Tile> targetables = new ArrayList<>(Arrays.asList(
                game.getTile(new Point(0, 1)),
                game.getTile(new Point(1, 2)),
                game.getTile(new Point(2, 1)),
                game.getTile(new Point(3, 0))
                ));
        assertEquals(new HashSet<>(targetables), weaponController.areaSelection(green, 1, 2));
    }

}
