package it.polimi.se2019.controller;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import it.polimi.se2019.model.*;
import it.polimi.se2019.utility.BiSet;
import it.polimi.se2019.utility.Pair;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.vc_events.CalculatePointsEvent;
import it.polimi.se2019.view.vc_events.SpawnEvent;
import it.polimi.se2019.view.vc_events.VCMoveEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDeathController {
    private Game game= new Game();
    private TurnController turnController;
    Player leiva= new Player(new Figure(FigureColour.GREEN),game);
    Player wallace= new Player(new Figure(FigureColour.MAGENTA),game);
    Player ciro= new Player(new Figure(FigureColour.YELLOW),game);
    TestModelHelper testModelHelper=new TestModelHelper();
    DeathController deathController=new DeathController(game);
    private KillshotTrack killshotTrack= new KillshotTrack(1);
    @Before
    public void setup(){
        BiSet<FigureColour, String> look= new BiSet<>();
        List<Player> playerList=new ArrayList<>();
        look.add(new Pair<>(FigureColour.GREEN, "leiva"));
        look.add(new Pair<>(FigureColour.MAGENTA, "wallace"));
        look.add(new Pair<>(FigureColour.YELLOW, "ciro"));
        playerList.add(leiva);
        playerList.add(ciro);
        playerList.add(wallace);
        game.setUserLookup(look);
        game.setPlayers(playerList);
        game.register(testModelHelper);
        game.setGameMap(new GameMap("Small"));
        List<String> users= new ArrayList<>();
        users.add(game.playerToUser(leiva));
        users.add(game.playerToUser(wallace));
        users.add(game.playerToUser(ciro));
        game.setUsernames(users);
        turnController=new TurnController(game);
        LootCardHelper lootCardHelper=game.getLootCardHelper();
        Point casualLoot= new Point(1,1);
        game.getGameMap().getTile(casualLoot).add((LootCard)lootCardHelper.findByName("PBR"));
        leiva.getFigure().spawn(casualLoot);
        leiva.grabStuff("PBR");
        WeaponHelper weaponHelper=game.getWeaponHelper();
        Point redSpawnPoint=new Point(0,1);
        game.getGameMap().getTile(redSpawnPoint).add((Weapon)weaponHelper.findByName("Heatseeker"));
        wallace.getFigure().spawn(redSpawnPoint);
        wallace.grabStuff("Heatseeker");
        game.setKillshotTrack(killshotTrack);
        game.setFinalFrenzy(false);
    }

    @Test
    public void testRespawn(){
        Weapon heatseeker= wallace.getWeapons().get(0);
        List<Player> players=new ArrayList<>();
        players.add(leiva);
        String colour=leiva.getPowerUps().get(0).getColour();
        game.apply(game.playerToUser(wallace),players,heatseeker.getWeaponEffects().iterator().next().getEffects().iterator().next());
        game.apply(game.playerToUser(wallace),players,heatseeker.getWeaponEffects().iterator().next().getEffects().iterator().next());
        game.apply(game.playerToUser(wallace),players,heatseeker.getWeaponEffects().iterator().next().getEffects().iterator().next());
        game.apply(game.playerToUser(wallace),players,heatseeker.getWeaponEffects().iterator().next().getEffects().iterator().next());
        SpawnEvent spawnEvent= new SpawnEvent(game.playerToUser(leiva),colour,"");
        deathController.update(spawnEvent);
        assertEquals(0,leiva.getHp().size());
        assertEquals(colour,game.getTile(leiva.getPosition()).getColour().toString());
    }

    @Test
    public void testCalculatePointsEvent(){
        Weapon heatseeker= wallace.getWeapons().get(0);
        List<Player> players=new ArrayList<>();
        players.add(leiva);
        game.apply(game.playerToUser(wallace),players,heatseeker.getWeaponEffects().iterator().next().getEffects().iterator().next());
        game.apply(game.playerToUser(wallace),players,heatseeker.getWeaponEffects().iterator().next().getEffects().iterator().next());
        game.apply(game.playerToUser(wallace),players,heatseeker.getWeaponEffects().iterator().next().getEffects().iterator().next());
        game.apply(game.playerToUser(wallace),players,heatseeker.getWeaponEffects().iterator().next().getEffects().iterator().next());
        CalculatePointsEvent calculatePointsEvent=new CalculatePointsEvent(game.playerToUser(wallace));
        deathController.update(calculatePointsEvent);
        assertEquals(18,(int)wallace.getPoints());
    }

}
