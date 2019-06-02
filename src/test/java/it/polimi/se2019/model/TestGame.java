package it.polimi.se2019.model;

import it.polimi.se2019.utility.BiSet;
import it.polimi.se2019.utility.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestGame {

    private Game game= new Game();
    private List<Player> players= new ArrayList<>();
    private Player magenta= new Player(new Figure(FigureColour.MAGENTA), game);
    private Player blue= new Player(new Figure(FigureColour.BLUE), game);
    private Player yellow= new Player(new Figure(FigureColour.YELLOW), game);
    private Player grey= new Player(new Figure(FigureColour.GREY), game);
    private Player green= new Player(new Figure(FigureColour.GREEN), game);
    private BiSet<FigureColour, String> lookup= new BiSet<>();

    @Before
    public void setup(){
        players.add(magenta);
        players.add(blue);
        players.add(grey);
        players.add(green);
        players.add(yellow);
        game.setPlayers(players);

        lookup.add(new Pair<>(FigureColour.MAGENTA, "magenta"));
        lookup.add(new Pair<>(FigureColour.BLUE, "blue"));
        lookup.add(new Pair<>(FigureColour.YELLOW, "yellow"));
        lookup.add(new Pair<>(FigureColour.GREEN, "green"));
        lookup.add(new Pair<>(FigureColour.GREY, "grey"));
        game.setUserLookup(lookup);

    }

    @Test
    public void testColourToUser(){

        assertEquals(game.colourToUser(FigureColour.MAGENTA), "magenta");
    }

    @Test
    public void testColourToPlayer(){

        assertEquals(game.colourToPlayer(FigureColour.MAGENTA), magenta);
    }

    @Test
    public void testUserToPlayer(){

        assertEquals(game.userToPlayer("magenta"), magenta);
    }

    @Test
    public void testUpdatePoints(){

        game.updatePoints("magenta",1);
        assertEquals(magenta.getPoints(), (Integer) 1);
    }

}
