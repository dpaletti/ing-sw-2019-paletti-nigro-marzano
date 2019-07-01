package it.polimi.se2019.server.controller;

import it.polimi.se2019.commons.mv_events.StartFirstTurnEvent;
import it.polimi.se2019.commons.mv_events.TurnEvent;
import it.polimi.se2019.commons.utility.BiSet;
import it.polimi.se2019.commons.utility.Pair;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.commons.vc_events.*;
import it.polimi.se2019.server.model.*;
import it.polimi.se2019.server.network.Server;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestTurnController {
    private Game game= new Game();
    private TurnController turnController;
    Player leiva= new Player(new Figure(FigureColour.GREEN),game);
    Player wallace= new Player(new Figure(FigureColour.MAGENTA),game);
    Player ciro= new Player(new Figure(FigureColour.YELLOW),game);
    TestModelHelper testModelHelper=new TestModelHelper();
    Server server=new Server(1);

    @Before
    public void setup(){
        BiSet<FigureColour, String> lookup= new BiSet<>();
        List<Player> playerList=new ArrayList<>();
        lookup.add(new Pair<>(FigureColour.GREEN, "leiva"));
        lookup.add(new Pair<>(FigureColour.MAGENTA, "wallace"));
        lookup.add(new Pair<>(FigureColour.YELLOW, "ciro"));
        playerList.add(leiva);
        playerList.add(ciro);
        playerList.add(wallace);
        game.setUserLookup(lookup);
        game.setPlayers(playerList);
        game.register(testModelHelper);
        game.setGameMap(new GameMap("Small"));
        List<String> users= new ArrayList<>();
        users.add(game.playerToUser(leiva));
        users.add(game.playerToUser(wallace));
        users.add(game.playerToUser(ciro));
        game.setUsernames(users);
        turnController=new TurnController(game,server);
        WeaponHelper weaponHelper=game.getWeaponHelper();
        Point redSpawnPoint= new Point(0,1);
        game.getGameMap().getTile(redSpawnPoint).add((Weapon)weaponHelper.findByName("Heatseeker"));
        leiva.getFigure().spawn(redSpawnPoint);
        leiva.grabStuff("Heatseeker");
        leiva.getWeapons().get(0).setLoaded(false);
    }

    @Test
    public void testSpawnEventDispatch(){

        SpawnEvent event= new SpawnEvent(game.playerToUser(leiva),"Red","TeleportBlue");
        turnController.update(event);
        assertEquals(new Point(0,1),leiva.getPosition());
        List<ArrayList<PartialCombo>> partialCombos=leiva.getHealthState().getMoves();
        TurnEvent message= (TurnEvent)testModelHelper.getCurrent();
    }

    @Test
    public void testReloadEventDispatch(){
       ReloadEvent event= new ReloadEvent(game.playerToUser(leiva),new ArrayList<>(Arrays.asList("Heatseeker")));
       turnController.update(event);
       assertTrue(leiva.getWeapons().get(0).getLoaded());
       assertEquals(0,turnController.getComboIndex());
        StartFirstTurnEvent turnEvent= (StartFirstTurnEvent)testModelHelper.getCurrent();
        assertEquals("wallace",turnEvent.getDestination());
    }

    @Test
    public void testChosenComboEventDispatch(){
        ComboHelper comboHelper= game.getComboHelper();
        ChosenComboEvent event= new ChosenComboEvent(game.playerToUser(leiva),"FrenzyMoveReloadShoot");
        turnController.update(event);
        VCMoveEvent moveChoice= new VCMoveEvent(game.playerToUser(leiva),new Point(0,2),false);
        turnController.update(moveChoice);
        ReloadEvent reloadEvent= new ReloadEvent(game.playerToUser(leiva),new ArrayList<>(Arrays.asList("Heatseeker")));
        turnController.update(reloadEvent);
        assertEquals(new Point(0,2),leiva.getPosition());
        assertTrue(leiva.getWeapons().get(0).getLoaded());
    }

    @Test
    public void testEndTurn(){
        ComboHelper comboHelper= game.getComboHelper();
        ChosenComboEvent event=new ChosenComboEvent(game.playerToUser(leiva), "RunAround");
        turnController.update(event);
        VCMoveEvent moveEvent= new VCMoveEvent(game.playerToUser(leiva),new Point(0,2),false);
        turnController.update(moveEvent);
        VCMoveEvent moveEvent2= new VCMoveEvent(game.playerToUser(leiva),new Point(1,2),false);
        turnController.update(moveEvent2);
        VCMoveEvent moveEvent3= new VCMoveEvent(game.playerToUser(leiva),new Point(2,2),false);
        turnController.update(moveEvent3);
        assertEquals(new Point(2,2),leiva.getPosition());
        turnController.update(event);
        turnController.update(moveEvent2);
        turnController.update(moveEvent3);
        turnController.update(moveEvent2);
        assertEquals(new Point(1,2),leiva.getPosition());
        VCEndOfTurnEvent endOfTurnEvent= new VCEndOfTurnEvent(game.playerToUser(leiva));
        turnController.update(endOfTurnEvent);
        assertEquals("wallace",turnController.getCurrentPlayer());
    }

    @Test
    public void testGrabEvent(){
        ComboHelper comboHelper= game.getComboHelper();
        LootCardHelper lootCardHelper= game.getLootCardHelper();
        ChosenComboEvent chosenComboEvent= new ChosenComboEvent(game.playerToUser(leiva), "GrabStuff");
        game.getGameMap().getTile(new Point(0,2)).add((LootCard)lootCardHelper.findByName("PBR"));
        VCMoveEvent moveEvent= new VCMoveEvent(game.playerToUser(leiva),new Point(0,2),false);
        turnController.update(chosenComboEvent);
        turnController.update(moveEvent);
        assertEquals(new Point(0,2),leiva.getPosition());
        GrabEvent grabEvent= new GrabEvent(game.playerToUser(leiva),"PBR");
        turnController.update(grabEvent);
    }


}
