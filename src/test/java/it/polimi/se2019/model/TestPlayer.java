package it.polimi.se2019.model;

import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.BiSet;
import it.polimi.se2019.utility.Pair;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class TestPlayer {

    private Game game= new Game();
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
    public void testAddMark (){
        Player player= new Player(new Figure(FigureColour.GREEN), game);

        Tear tear= new Tear(FigureColour.MAGENTA);
        player.addMark(FigureColour.MAGENTA);

        assertTrue(player.getMarks().contains(tear));
    }

    @Test
    public void testAddTear (){
        Player player= new Player(new Figure(FigureColour.BLUE), game);
        List<Tear> finalHP = new ArrayList<>();
        Tear tear= new Tear(FigureColour.GREEN);
        finalHP.add(tear);
        player.setHp(finalHP);
        player.addTear(FigureColour.MAGENTA);

        assertEquals(player.getHp().get(1), new Tear (FigureColour.MAGENTA));
    }

}
