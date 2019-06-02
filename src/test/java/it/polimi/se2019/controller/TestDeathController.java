package it.polimi.se2019.controller;
import it.polimi.se2019.model.*;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.BiSet;
import it.polimi.se2019.utility.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.*;
@RunWith(MockitoJUnitRunner.class)
public class TestDeathController {

    @Mock
    private Server server;

    private Game game= new Game();
    private DeathController deathController= new DeathController(server, 1, game);
    private List<Integer> points= new ArrayList<>(Arrays.asList(8, 6, 4, 2, 1, 1));
    private List<Player> players= new ArrayList<>();
    private List<FigureColour> figureColours= new ArrayList<>(Arrays.asList(FigureColour.BLUE, FigureColour.GREEN, FigureColour.GREY));
    private Player magenta= new Player(new Figure(FigureColour.MAGENTA), game);
    private Player blue= new Player(new Figure(FigureColour.BLUE), game);
    private Player yellow= new Player(new Figure(FigureColour.YELLOW), game);
    private Player grey= new Player(new Figure(FigureColour.GREY), game);
    private Player green= new Player(new Figure(FigureColour.GREEN), game);
    private BiSet<FigureColour, String> lookup= new BiSet<>();
    private List<Tear> hp= new ArrayList<>(Arrays.asList(new Tear(FigureColour.BLUE),
            new Tear(FigureColour.BLUE),
            new Tear(FigureColour.GREEN),
            new Tear(FigureColour.GREEN),
            new Tear(FigureColour.GREY),
            new Tear(FigureColour.YELLOW),
            new Tear(FigureColour.BLUE),
            new Tear(FigureColour.YELLOW),
            new Tear(FigureColour.GREEN),
            new Tear(FigureColour.YELLOW),
            new Tear(FigureColour.GREY)
    ));


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
    public void testSolveTies(){

        deathController.solveTies(points, figureColours, hp);
        assertEquals((Integer) 8, blue.getPoints());
        assertEquals((Integer) 6, green.getPoints());
    }

    @Test
    public void testCalculateHits(){

        Map<FigureColour, Integer> leaderboard= new HashMap<>();
        leaderboard.put(FigureColour.BLUE, 3);
        leaderboard.put(FigureColour.GREEN, 3);
        leaderboard.put(FigureColour.GREY, 2);
        leaderboard.put(FigureColour.YELLOW, 3);
        Map<FigureColour, Integer> calculatedLeaderboard= deathController.calculateHits(hp);

        assertEquals(calculatedLeaderboard, leaderboard);
    }

    @Test
    public void testOverkill(){

    assertFalse(deathController.overkill(hp));
    hp.add(new Tear(FigureColour.BLUE));
    assertTrue(deathController.overkill(hp));
    }

    @Test
    public void testAssignPoints(){

        deathController.assignPoints("magenta", hp);
        assertEquals(blue.getPoints(), (Integer) 9);
        assertEquals(green.getPoints(), (Integer) 6);
        assertEquals(yellow.getPoints(), (Integer) 4);
        assertEquals(grey.getPoints(), (Integer) 2);
    }
}
