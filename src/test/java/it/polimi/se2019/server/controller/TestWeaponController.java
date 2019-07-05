package it.polimi.se2019.server.controller;

import it.polimi.se2019.commons.mv_events.AllowedWeaponsEvent;
import it.polimi.se2019.commons.mv_events.MVCardEndEvent;
import it.polimi.se2019.commons.mv_events.PartialSelectionEvent;
import it.polimi.se2019.commons.mv_events.PossibleEffectsEvent;
import it.polimi.se2019.commons.utility.BiSet;
import it.polimi.se2019.commons.utility.Pair;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.commons.vc_events.ChosenComboEvent;
import it.polimi.se2019.commons.vc_events.ChosenEffectEvent;
import it.polimi.se2019.commons.vc_events.ChosenWeaponEvent;
import it.polimi.se2019.commons.vc_events.VCPartialEffectEvent;
import it.polimi.se2019.server.model.*;
import it.polimi.se2019.server.network.Server;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class TestWeaponController {

    private Game game = new Game();
    private List<Player> players = new ArrayList<>();
    private WeaponController weaponController;
    private TurnController turnController;
    private CardController cardController;
    private Player magenta = new Player(new Figure(FigureColour.MAGENTA), game);
    private Player blue = new Player(new Figure(FigureColour.BLUE), game);
    private Player yellow = new Player(new Figure(FigureColour.YELLOW), game);
    private Player grey = new Player(new Figure(FigureColour.GREY), game);
    private Player green = new Player(new Figure(FigureColour.GREEN), game);
    private TurnMemory turnMemory = new TurnMemory();
    private List<Player> hit = new ArrayList<>(Arrays.asList(magenta));
    private Map<String, List<Player>> hitPlayers = new HashMap<>();
    private BiSet<FigureColour, String> lookup = new BiSet<>();
    private Weapon furnace= new Weapon(Paths.get("files/weapons/Furnace.json").toString());
    private Weapon shotgun= new Weapon(Paths.get("files/weapons/Shotgun.json").toString());
    private Weapon railgun= new Weapon(Paths.get("files/weapons/Railgun.json").toString());

    private PartialWeaponEffect partial;

    private ChosenWeaponEvent chosenWeaponEvent = new ChosenWeaponEvent("magenta", "Whisper");
    private ChosenEffectEvent chosenEffectEvent = new ChosenEffectEvent("magenta", "effect", "Whisper");
    private TestModelHelper testModelHelper = new TestModelHelper();
    private Server server = new Server(1);

    @Before
    public void setup() {
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
        turnController = new TurnController(game, server);
        weaponController = new WeaponController(game);
        cardController = new CardController(game);
    }

    @Test
    public void testUseFurnace(){
        //Magenta is grabbing the furnace
        magenta.getFigure().spawn(new Point(3, 0));
        game.getGameMap().getTile(new Point(3, 0)).add(furnace);
        magenta.grabStuff(furnace.getName());
        //Putting magenta in strategic place
        magenta.getFigure().spawn(new Point(2, 1));
        yellow.getFigure().spawn(new Point(2, 2));
        green.getFigure().spawn(new Point(1,2));
        //Magenta decided to shoot and sends a ChosenComboEvent
        ChosenComboEvent shoot = new ChosenComboEvent(game.playerToUser(magenta), "ShootPeople");
        turnController.update(shoot);
        //The turnController is sending back a AllowedWeaponsEvent
        AllowedWeaponsEvent availableWeapons = (AllowedWeaponsEvent) testModelHelper.getCurrent();
        assertEquals("Furnace", availableWeapons.getWeapons().get(0));
        //Now magenta will choose is only weapon sending a ChosenWeaponEvent
        ChosenWeaponEvent chosen= new ChosenWeaponEvent(game.playerToUser(magenta),"Furnace");
        weaponController.update(chosen);
        //The weaponController is sending back a PossibleEffectsEvent,sending a possible WeaponEffect
        PossibleEffectsEvent possibles = (PossibleEffectsEvent) testModelHelper.getCurrent();
        assertEquals("Furnace",chosen.getWeapon());
        //Now i have to send a ChosenEffectEvent
        ChosenEffectEvent effectEvent= new ChosenEffectEvent(game.playerToUser(magenta),"basicMode","Furnace");
        weaponController.update(effectEvent);
        //The controller sends back a PartialSelectionEvent
        PartialSelectionEvent partialSelectionEvent = (PartialSelectionEvent) testModelHelper.getCurrent();
        assertTrue(partialSelectionEvent.getTargetTiles().contains(new Point(2,2)));
        assertTrue(partialSelectionEvent.getTargetTiles().contains(new Point(1,2)));
        VCPartialEffectEvent partialEffectEvent = new VCPartialEffectEvent(game.playerToUser(magenta),new Point(1,2), true);
        weaponController.update(partialEffectEvent);
        assertEquals(2, yellow.getHp().size());
        assertEquals(1, green.getHp().size());

    }

    @Test
    public void testUseShotgun(){
        //Magenta is grabbing the furnace
        magenta.getFigure().spawn(new Point(3, 0));
        game.getGameMap().getTile(new Point(3, 0)).add(shotgun);
        magenta.grabStuff(shotgun.getName());
        //Putting yellow in strategic place
        yellow.getFigure().spawn(new Point(3, 0));
        //Magenta decided to shoot and sends a ChosenComboEvent
        ChosenComboEvent shoot = new ChosenComboEvent(game.playerToUser(magenta), "ShootPeople");
        turnController.update(shoot);
        //The turnController is sending back a AllowedWeaponsEvent
        AllowedWeaponsEvent availableWeapons = (AllowedWeaponsEvent) testModelHelper.getCurrent();
        assertEquals("Shotgun", availableWeapons.getWeapons().get(0));
        //Now magenta will choose is only weapon sending a ChosenWeaponEvent
        ChosenWeaponEvent chosen= new ChosenWeaponEvent(game.playerToUser(magenta),"Shotgun");
        weaponController.update(chosen);
        //The weaponController is sending back a PossibleEffectsEvent,sending a possible WeaponEffect
        PossibleEffectsEvent possibles = (PossibleEffectsEvent) testModelHelper.getCurrent();
        assertEquals("Shotgun",chosen.getWeapon());
        //Now i have to send a ChosenEffectEvent
        ChosenEffectEvent effectEvent= new ChosenEffectEvent(game.playerToUser(magenta),"basicMode","Shotgun");
        weaponController.update(effectEvent);
        //The controller sends back a PartialSelectionEvent
        //The controller sends back a PartialSelectionEvent
        PartialSelectionEvent partialSelectionEvent = (PartialSelectionEvent) testModelHelper.getCurrent();
        assertTrue(partialSelectionEvent.getTargetPlayers().contains(game.playerToUser(yellow)));
        VCPartialEffectEvent partialEffectEvent = new VCPartialEffectEvent(game.playerToUser(magenta),game.playerToUser(yellow), true);
        weaponController.update(partialEffectEvent);
        assertEquals(3, yellow.getHp().size());
        VCPartialEffectEvent skip = new VCPartialEffectEvent(game.playerToUser(magenta), true);
        weaponController.update(skip);
        MVCardEndEvent cardEndEvent= (MVCardEndEvent)testModelHelper.getCurrent();
        assertTrue(cardEndEvent.isWeapon());

    }

    @Test
    public void testRailgun(){
        //Magenta is grabbing the railgun
        magenta.getFigure().spawn(new Point(3, 0));
        game.getGameMap().getTile(new Point(3, 0)).add(railgun);
        magenta.grabStuff(railgun.getName());
        //Putting yellow in strategic place
        yellow.getFigure().spawn(new Point(1, 0));
        //Magenta decided to shoot and sends a ChosenComboEvent
        ChosenComboEvent shoot = new ChosenComboEvent(game.playerToUser(magenta), "ShootPeople");
        turnController.update(shoot);
        //The turnController is sending back a AllowedWeaponsEvent
        AllowedWeaponsEvent availableWeapons = (AllowedWeaponsEvent) testModelHelper.getCurrent();
        assertEquals("Railgun", availableWeapons.getWeapons().get(0));
        //Now magenta will choose is only weapon sending a ChosenWeaponEvent
        ChosenWeaponEvent chosen= new ChosenWeaponEvent(game.playerToUser(magenta),"Railgun");
        weaponController.update(chosen);
        //Now i have to send a ChosenEffectEvent
        ChosenEffectEvent effectEvent= new ChosenEffectEvent(game.playerToUser(magenta),"basicMode","Railgun");
        weaponController.update(effectEvent);
        //The controller sends back a PartialSelectionEvent
        PartialSelectionEvent partialSelectionEvent = (PartialSelectionEvent) testModelHelper.getCurrent();
        assertTrue(partialSelectionEvent.getTargetTiles().contains(new Point(3,0)));
        assertTrue(partialSelectionEvent.getTargetTiles().contains(new Point(2,0)));
        assertTrue(partialSelectionEvent.getTargetTiles().contains(new Point(1,0)));
        assertTrue(partialSelectionEvent.getTargetTiles().contains(new Point(0,0)));
        assertTrue(partialSelectionEvent.getTargetTiles().contains(new Point(3,1)));
        assertTrue(partialSelectionEvent.getTargetTiles().contains(new Point(3,2)));
        VCPartialEffectEvent partialEffectEvent = new VCPartialEffectEvent(game.playerToUser(magenta),new Point(1,0), true);
        weaponController.update(partialEffectEvent);
        MVCardEndEvent cardEndEvent= (MVCardEndEvent)testModelHelper.getCurrent();
    }


}


