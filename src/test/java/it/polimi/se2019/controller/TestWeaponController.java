package it.polimi.se2019.controller;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.BiSet;
import it.polimi.se2019.utility.Pair;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.vc_events.*;
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
    private Weapon shockwave = new Weapon(Paths.get("files/weapons/Shockwave.json").toString());

    private PartialWeaponEffect partial;

    private ChosenWeaponEvent chosenWeaponEvent = new ChosenWeaponEvent("magenta", "Whisper");
    private ChosenEffectEvent chosenEffectEvent = new ChosenEffectEvent("magenta", "effect", "Whisper");
    private TestModelHelper testModelHelper = new TestModelHelper();
    private Server server=new Server(1);

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

        List<String> usernames= new ArrayList<>();
        for (Player player:players)
            usernames.add(game.playerToUser(player));
        game.setUsernames(usernames);

        hitPlayers.put("basicMode", hit);
        turnMemory.setHitTargets(hitPlayers);
        turnMemory.setLastEffectUsed("basicMode");
        game.setTurnMemory(turnMemory);
        turnController=new TurnController(game,server);
        weaponController=new WeaponController(game);
        cardController=new CardController(game);
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

    //Testing the use of the whisper in the case of choosing to use the basic effect and to skip it
    @Test
    public void testUseWeaponWhisper(){
        //Magenta is grabbing the whisper
        magenta.getFigure().spawn(new Point(3,0));
        game.getGameMap().getTile(new Point(3,0)).add(whisper);
        magenta.grabStuff(whisper.getName());
        //Putting magenta in strategic place
        magenta.getFigure().spawn(new Point(1,0));
        yellow.getFigure().spawn(new Point(3,1));
        //Magenta decided to shoot and sends a ChosenComboEvent
        ChosenComboEvent shoot= new ChosenComboEvent(game.playerToUser(magenta),"ShootPeople");
        turnController.update(shoot);
        //The turnController is sending back a AllowedWeaponsEvent
        AllowedWeaponsEvent availableWeapons= (AllowedWeaponsEvent)testModelHelper.getCurrent();
        assertEquals("Whisper",availableWeapons.getWeapons().get(0));
        //Now magenta will choose is only weapon sending a ChosenWeaponEvent
        weaponController.update(chosenWeaponEvent);
        //The weaponController is sending back a PossibleEffectsEvent,sending a possible WeaponEffect
        PossibleEffectsEvent possibles=(PossibleEffectsEvent)testModelHelper.getCurrent();
        assertEquals("effect",possibles.getEffects().keySet().iterator().next());
        //Now i have to send a ChosenEffectEvent
        weaponController.update(chosenEffectEvent);
        //The controller sends back a PartialSelectionEvent
        PartialSelectionEvent partialSelectionEvent= (PartialSelectionEvent)testModelHelper.getCurrent();
        assertEquals(game.playerToUser(yellow),partialSelectionEvent.getTargetPlayers().get(0));
        //Now magenta has to send a VCPartialEffectEvent to specify the target or skip
        VCPartialEffectEvent partialEffectEvent= new VCPartialEffectEvent(game.playerToUser(magenta),game.playerToUser(yellow));
        weaponController.update(partialEffectEvent);
        assertEquals(3,yellow.getHp().size());
        assertEquals(1,yellow.getMarks().size());
        //The weapon is ended and so a MVWeaponEndEvent is being sent to magenta
        MVWeaponEndEvent event= (MVWeaponEndEvent)testModelHelper.getCurrent();
        assertEquals(game.playerToUser(magenta),event.getDestination());
        //Now i want to test the skip case
        turnController.update(shoot);
        weaponController.update(chosenWeaponEvent);
        weaponController.update(chosenEffectEvent);
        VCPartialEffectEvent skip= new VCPartialEffectEvent(game.playerToUser(magenta));
        weaponController.update(skip);
        MVWeaponEndEvent end= (MVWeaponEndEvent)testModelHelper.getCurrent();
        assertEquals(game.playerToUser(magenta),end.getDestination());
    }

    @Test
    public void testUseShockwave(){
        //Magenta is grabbing the shockwave
        magenta.getFigure().spawn(new Point(3,0));
        game.getGameMap().getTile(new Point(3,0)).add(shockwave);
        magenta.grabStuff(shockwave.getName());
        //Setting the targets in the right places
        green.getFigure().spawn(new Point(2,0));
        blue.getFigure().spawn(new Point(3,1));
        //Magenta decided to shoot and sends a ChosenComboEvent
        ChosenComboEvent shoot= new ChosenComboEvent(game.playerToUser(magenta),"ShootPeople");
        turnController.update(shoot);
        //The turnController is sending back a AllowedWeaponsEvent
        AllowedWeaponsEvent availableWeapons= (AllowedWeaponsEvent)testModelHelper.getCurrent();
        assertEquals("Shockwave",availableWeapons.getWeapons().get(0));
        ChosenWeaponEvent chosenShockwave = new ChosenWeaponEvent("magenta", "Shockwave");
        weaponController.update(chosenShockwave);
        //The weaponController is sending back a PossibleEffectsEvent,sending a possible WeaponEffect
        PossibleEffectsEvent possibles=(PossibleEffectsEvent)testModelHelper.getCurrent();
        HashMap<String,Integer> effectMap= new HashMap<>();
        effectMap.put("basicMode",-1);
        effectMap.put("inTsunamiMode",0);
        assertEquals(effectMap,possibles.getEffects());
        //Now i have to send a ChosenEffectEvent
        ChosenEffectEvent chosenBasic=new ChosenEffectEvent(game.playerToUser(magenta),"basicMode","Shockwave");
        weaponController.update(chosenBasic);
        //The controller sends back a PartialSelectionEvent
        PartialSelectionEvent partialSelectionEvent= (PartialSelectionEvent)testModelHelper.getCurrent();
        assertTrue(partialSelectionEvent.getTargetTiles().contains(new Point(2,0)));
        assertTrue(partialSelectionEvent.getTargetTiles().contains(new Point(3,1)));
        //Now magenta has to send a VCPartialEffectEvent to specify the target or skip
        VCPartialEffectEvent partialEffectEvent= new VCPartialEffectEvent(game.playerToUser(magenta),new Point(2,0));
        weaponController.update(partialEffectEvent);
        assertEquals(1,green.getHp().size());
        //Now the controller sends back another PartialSelectionEvent
        PartialSelectionEvent secondTargetset= (PartialSelectionEvent)testModelHelper.getCurrent();
        assertTrue(secondTargetset.getTargetTiles().contains(new Point(3,1)));
        //Now magenta has to send a VCPartialEffectEvent to specify the target or skip
        VCPartialEffectEvent skip= new VCPartialEffectEvent(game.playerToUser(magenta));
        weaponController.update(skip);
        //The weapon is ended and so a MVWeaponEndEvent is being sent to magenta
        MVWeaponEndEvent event= (MVWeaponEndEvent)testModelHelper.getCurrent();
        assertEquals(game.playerToUser(magenta),event.getDestination());
        //Now i want to try the second mode of the shockwave
        ChosenComboEvent shootSecondMode= new ChosenComboEvent(game.playerToUser(magenta),"ShootPeople");
        VCWeaponEndEvent endEvent=new VCWeaponEndEvent(game.playerToUser(magenta));
        weaponController.update(endEvent);
        turnController.update(shoot);
        //The turnController is sending back a AllowedWeaponsEvent
        AllowedWeaponsEvent weapons= (AllowedWeaponsEvent)testModelHelper.getCurrent();
        assertTrue(weapons.getWeapons().isEmpty());

    }




}
