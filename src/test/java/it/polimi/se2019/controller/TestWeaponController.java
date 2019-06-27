package it.polimi.se2019.controller;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.mv_events.AllowedMovementsEvent;
import it.polimi.se2019.model.mv_events.AvailableWeaponsEvent;
import it.polimi.se2019.model.mv_events.MVSelectionEvent;
import it.polimi.se2019.model.mv_events.PossibleEffectsEvent;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.*;
import it.polimi.se2019.view.vc_events.ChosenEffectEvent;
import it.polimi.se2019.view.vc_events.ChosenWeaponEvent;
import it.polimi.se2019.view.vc_events.ShootEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.Array;
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
    private TurnMemory turnMemory= new TurnMemory();
    private List<Player> hit = new ArrayList<>(Arrays.asList(magenta));
    private Map<String, List<Player>> hitPlayers = new HashMap<>();
    private BiSet<FigureColour, String> lookup= new BiSet<>();
    private Weapon whisper = new Weapon(Paths.get("files/weapons/Whisper.json").toString());
    private Weapon lockRifle = new Weapon(Paths.get("files/weapons/LockRifle.json").toString());
    private Weapon machineGun = new Weapon(Paths.get("files/weapons/MachineGun.json").toString());

    private PartialWeaponEffect partial;

    private ChosenWeaponEvent chosenWeaponEvent = new ChosenWeaponEvent("magenta", "Whisper");
    private ChosenEffectEvent chosenEffectEvent = new ChosenEffectEvent("magenta", "effect", "Whisper");
    private ShootEvent shootEvent = new ShootEvent("magenta");
    private TestModelHelper testModelHelper = new TestModelHelper();

    @Before
    public void setup (){
        game.setGameMap(new GameMap("Large"));
        magenta.getFigure().spawn(new Point(2, 0));
        green.getFigure().spawn(new Point(0, 0));
        blue.getFigure().spawn(new Point(1, 0));
        yellow.getFigure().spawn(new Point(1,2));
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

        hitPlayers.put("basicMode", hit);
        turnMemory.setHitTargets(hitPlayers);
        turnMemory.setLastEffectUsed("basicMode");
        game.setTurnMemory(turnMemory);
    }

    @Test
    public void testGenerateTargetSet(){
        //------------------------------------whisper----------------------------------------------
        partial = whisper.getDefinition().getListLayer(1).get(0).getKey().getEffectGraph().getListLayer(1).get(0).getKey();
        List<Player> targetables = new ArrayList<>(Arrays.asList(green));
        assertTrue(weaponController.generateTargetSet(partial , magenta).containsAll(targetables));

        //------------------------------------lock rifle-------------------------------------------
        partial = lockRifle.getDefinition().getListLayer(1).get(0).getKey().getEffectGraph().getListLayer(1).get(0).getKey();
        targetables = new ArrayList<>(Arrays.asList(grey));
        assertTrue(weaponController.generateTargetSet(partial, yellow).containsAll(targetables));

        hitPlayers.put("B1", hit);
        turnMemory.setHitTargets(hitPlayers);
        turnMemory.setLastEffectUsed("B1");
        game.setTurnMemory(turnMemory);
        partial = lockRifle.getDefinition().getListLayer(2).get(0).getKey().getEffectGraph().getListLayer(1).get(0).getKey();
        targetables = new ArrayList<>(Arrays.asList(grey));
        assertTrue(weaponController.generateTargetSet(partial, yellow).containsAll(targetables));

        //-----------------------------------machine gun-----------------------------------------
        partial = machineGun.getDefinition().getListLayer(1).get(0).getKey().getEffectGraph().getListLayer(1).get(0).getKey();
        targetables = new ArrayList<>(Arrays.asList(green, blue));
        assertTrue(weaponController.generateTargetSet(partial, magenta).containsAll(targetables));

        hit = new ArrayList<>(Arrays.asList(green, blue));
        hitPlayers.put("B1", hit);
        turnMemory.setHitTargets(hitPlayers);
        turnMemory.setLastEffectUsed("B1");
        game.setTurnMemory(turnMemory);
        partial = machineGun.getDefinition().getListLayer(2).get(0).getKey().getEffectGraph().getListLayer(1).get(0).getKey();
        targetables = new ArrayList<>(Arrays.asList(green, blue));
        assertTrue(weaponController.generateTargetSet(partial, magenta).containsAll(targetables));

        hit = new ArrayList<>(Arrays.asList(green));
        hitPlayers.put("B2", hit);
        turnMemory.setHitTargets(hitPlayers);
        turnMemory.setLastEffectUsed("B2");
        game.setTurnMemory(turnMemory);
        partial = machineGun.getDefinition().getListLayer(3).get(0).getKey().getEffectGraph().getListLayer(1).get(0).getKey();
        targetables = new ArrayList<>(Arrays.asList(blue));
        assertTrue(weaponController.generateTargetSet(partial, magenta).containsAll(targetables));
    }

    @Test
    public void testDispatchChosenEffect(){
        weaponController.dispatch(chosenEffectEvent);
        ArrayList<ArrayList<String>> targets = new ArrayList<>();
        targets.add(new ArrayList<>(Arrays.asList("green")));
        assertEquals("magenta", testModelHelper.getCurrent().getDestination());
        assertTrue(((MVSelectionEvent)testModelHelper.getCurrent()).getActionOnTiles().isEmpty());
        assertTrue(((MVSelectionEvent)testModelHelper.getCurrent()).getActionOnPlayers().values().contains(targets));
    }

    @Test
    public void testDispatchShoot(){
        magenta.getFigure().spawn(new Point(0, 1));
        ArrayList<Grabbable> grabbables = new ArrayList<>(Arrays.asList(whisper));
        game.getTile(magenta.getPosition()).setGrabbables(grabbables);
        magenta.grabStuff("Whisper");
        weaponController.dispatch(shootEvent);

        assertEquals("magenta", testModelHelper.getCurrent().getDestination());
        assertTrue(((AvailableWeaponsEvent)testModelHelper.getCurrent()).getWeapons().contains("Whisper"));
    }

    @Test
    public void testDispatchWeapon(){
        int value = 0;
        weaponController.dispatch(shootEvent);
        weaponController.dispatch(chosenWeaponEvent);

        assertEquals("magenta", testModelHelper.getCurrent().getDestination());
        assertEquals("Whisper", ((PossibleEffectsEvent)testModelHelper.getCurrent()).getWeaponName());
        assertTrue(((PossibleEffectsEvent)testModelHelper.getCurrent()).getEffects().get("effect") == 0);
        assertTrue(((PossibleEffectsEvent)testModelHelper.getCurrent()).getEffects().containsKey("effect"));
    }
}
