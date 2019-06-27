package it.polimi.se2019.controller;
import static org.junit.Assert.*;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.mv_events.NotEnoughPlayersConnectedEvent;
import it.polimi.se2019.model.mv_events.TurnEvent;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.BiSet;
import it.polimi.se2019.utility.Pair;
import it.polimi.se2019.utility.PartialCombo;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestTurnController {
    private Game game= new Game();
    private TurnController turnController;
    Player leiva= new Player(new Figure(FigureColour.GREEN),game);
    Player wallace= new Player(new Figure(FigureColour.MAGENTA),game);
    TestModelHelper testModelHelper=new TestModelHelper();

    @Before
    public void setup(){
        BiSet<FigureColour, String> lookup= new BiSet<>();
        List<Player> playerList=new ArrayList<>();
        lookup.add(new Pair<>(FigureColour.GREEN, "leiva"));
        lookup.add(new Pair<>(FigureColour.MAGENTA, "wallace"));
        playerList.add(leiva);
        playerList.add(wallace);
        game.setUserLookup(lookup);
        game.setPlayers(playerList);
        game.register(testModelHelper);
        game.setGameMap(new GameMap("Small"));
        List<String> users= new ArrayList<>();
        users.add(game.playerToUser(leiva));
        users.add(game.playerToUser(wallace));
        game.setUsernames(users);
        turnController=new TurnController(game);
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
        assertEquals(leiva.getHealthState().getMoves(),message.getCombos());
    }

    @Test
    public void testReloadEventDispatch(){
       ReloadEvent event= new ReloadEvent(game.playerToUser(leiva),"Heatseeker");
       turnController.update(event);
       assertTrue(leiva.getWeapons().get(0).getLoaded());
       assertEquals(0,turnController.getComboIndex());
       NotEnoughPlayersConnectedEvent message= (NotEnoughPlayersConnectedEvent)testModelHelper.getCurrent();
       assertEquals("*",message.getDestination());

    }

    @Test
    public void testChosenComboEventDispatch(){
        ComboHelper comboHelper= game.getComboHelper();
        Combo finalFrenzyMoveReloadShoot= (Combo)comboHelper.findByName("FrenzyMoveReloadShoot");
        ChosenComboEvent event= new ChosenComboEvent(game.playerToUser(leiva),finalFrenzyMoveReloadShoot);
        turnController.update(event);
        VCMoveEvent moveChoice= new VCMoveEvent(game.playerToUser(leiva),new Point(0,2),false);
        turnController.update(moveChoice);
        ReloadEvent reloadEvent= new ReloadEvent(game.playerToUser(leiva),"Heatseeker");
        turnController.update(reloadEvent);
        assertEquals(new Point(0,2),leiva.getPosition());
        assertTrue(leiva.getWeapons().get(0).getLoaded());
    }

    @Test
    public void testEndTurn(){
        ComboHelper comboHelper= game.getComboHelper();
        Combo run= (Combo)comboHelper.findByName("RunAround");
        ChosenComboEvent event=new ChosenComboEvent(game.playerToUser(leiva),run);
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
        EndOfTurnEvent endOfTurnEvent= new EndOfTurnEvent(game.playerToUser(leiva));
        turnController.update(endOfTurnEvent);
        NotEnoughPlayersConnectedEvent message= (NotEnoughPlayersConnectedEvent)testModelHelper.getCurrent();
        assertEquals("*",message.getDestination());

    }

    @Test
    public void testGrabEvent(){
        ComboHelper comboHelper= game.getComboHelper();
        Combo moveAndGrab= (Combo)comboHelper.findByName("GrabStuff");
        LootCardHelper lootCardHelper= game.getLootCardHelper();
        ChosenComboEvent chosenComboEvent= new ChosenComboEvent(game.playerToUser(leiva),moveAndGrab);
        leiva.getFigure().spawn(new Point(2,1));
        game.getGameMap().getTile(new Point(2,1)).add((LootCard)lootCardHelper.findByName("PBR"));
        turnController.update(chosenComboEvent);
        GrabEvent grabEvent= new GrabEvent(game.playerToUser(leiva),"PBR");
        turnController.update(grabEvent);
    }

}
