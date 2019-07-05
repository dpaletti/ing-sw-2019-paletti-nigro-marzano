package it.polimi.se2019.server.controller;

import it.polimi.se2019.commons.mv_events.PartialSelectionEvent;
import it.polimi.se2019.commons.mv_events.PossibleEffectsEvent;
import it.polimi.se2019.commons.utility.BiSet;
import it.polimi.se2019.commons.utility.Pair;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.commons.vc_events.ChosenEffectPowerUpEvent;
import it.polimi.se2019.commons.vc_events.PowerUpUsageEvent;
import it.polimi.se2019.commons.vc_events.VCChooseAmmoToPayEvent;
import it.polimi.se2019.commons.vc_events.VCPartialEffectEvent;
import it.polimi.se2019.server.model.*;
import it.polimi.se2019.server.network.Server;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;


public class TestPowerUpController {
    private Game game = new Game();
    private List<Player> players = new ArrayList<>();
    private PowerUpController powerUpController = new PowerUpController(game);
    private CardController cardController;
    private Player magenta= new Player(new Figure(FigureColour.MAGENTA), game);
    private Player blue= new Player(new Figure(FigureColour.BLUE), game);
    private Player yellow= new Player(new Figure(FigureColour.YELLOW), game);
    private Player grey= new Player(new Figure(FigureColour.GREY), game);
    private Player green= new Player(new Figure(FigureColour.GREEN), game);
    private TurnMemory turnMemory= new TurnMemory();
    private List<Player> hit = new ArrayList<>(Arrays.asList(magenta));
    private Map<String, List<Player>> hitPlayers = new HashMap<>();
    private BiSet<FigureColour, String> lookup= new BiSet<>();
    private PowerUp teleportBlue = new PowerUp(Paths.get("files/powerUps/TeleportBlue.json").toString());

    private PartialWeaponEffect partial;

    private PowerUpUsageEvent powerUpUsageEvent = new PowerUpUsageEvent("magenta", "TeleportBlue");
    private TestModelHelper testModelHelper = new TestModelHelper();
    private Server server=new Server(1);

    @Before
    public void setup () {
        game.setGameMap(new GameMap("Large"));
        magenta.getFigure().spawn(new Point(2, 0));
        green.getFigure().spawn(new Point(0, 0));
        blue.getFigure().spawn(new Point(1, 0));
        yellow.getFigure().spawn(new Point(1, 2));
        grey.getFigure().spawn(new Point(1, 1));

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
        game.register(testModelHelper);

        List<String> usernames = new ArrayList<>();
        for (Player player : players)
            usernames.add(game.playerToUser(player));
        game.setUsernames(usernames);

        hitPlayers.put("basicMode", hit);
        turnMemory.setHitTargets(hitPlayers);
        turnMemory.setLastEffectUsed("basicMode");
        game.setTurnMemory(turnMemory);
        cardController = new CardController(game);

    }

    @Test
    public void testPowerUpUsage(){
        magenta.getFigure().spawn(new Point(2, 0));
        game.getGameMap().getTile(new Point(2, 0)).add((LootCard)game.getLootCardHelper().findByName("PBY"));
        magenta.grabStuff("PBY");
        String name=magenta.getPowerUps().get(0).getName();
        PowerUpUsageEvent power= new PowerUpUsageEvent(game.playerToUser(magenta),name);
        powerUpController.dispatch(power);
        PossibleEffectsEvent possibleEffectsEvent= (PossibleEffectsEvent)testModelHelper.getCurrent();
        assertEquals(name,possibleEffectsEvent.getName());
        ChosenEffectPowerUpEvent chosen= new ChosenEffectPowerUpEvent(game.playerToUser(magenta),"effect",name);
        powerUpController.dispatch(chosen);
        PartialSelectionEvent partialSelectionEvent= (PartialSelectionEvent)testModelHelper.getCurrent();
        assertTrue(partialSelectionEvent.getTargetPlayers().contains("blue"));
        assertTrue(partialSelectionEvent.getTargetPlayers().contains("green"));
    }


}
