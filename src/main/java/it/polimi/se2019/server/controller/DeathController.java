package it.polimi.se2019.server.controller;

import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.mv_events.MVMoveEvent;
import it.polimi.se2019.commons.utility.JsonHandler;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.commons.vc_events.CalculatePointsEvent;
import it.polimi.se2019.commons.vc_events.DisconnectionEvent;
import it.polimi.se2019.commons.vc_events.SpawnEvent;
import it.polimi.se2019.server.model.FigureColour;
import it.polimi.se2019.server.model.Game;
import it.polimi.se2019.server.model.Player;
import it.polimi.se2019.server.model.Tear;
import it.polimi.se2019.server.network.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeathController extends AbstractDeathController{

    public DeathController(Server server, int roomNumber, Game model){
        super(model, server, roomNumber);
    }

    public DeathController(Game game){
        //Simple constructor for tests
        this.model=game;
    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //ignore events that this controller does not support
            Log.fine("DeathController ignored " + JsonHandler.serialize(message));
        }
    }

    @Override
    public void dispatch(SpawnEvent message) {
        List<Tear> hp= model.getHp(message.getSource());
        if (hp.size()<11)
            return;
        deathPointCalculation(message.getSource(), hp);
        model.userToPlayer(message.getSource()).emptyHp();
        respawn(message.getSource(),message.getDiscardedPowerUpColour());
    }

    @Override
    public void dispatch(CalculatePointsEvent message) {
        for (Player p: model.getPlayers())
            deathPointCalculation(message.getSource(), model.getHp(model.playerToUser(p)));
        if (model.isFinalFrenzy()){
            new FinalFrenzyDeathController(server, getRoomNumber(), model, this);
            return;
        }
        winnerPointCalculation(model.getKillshotTrack().getKillshot());
    }

    private boolean overkill (List<Tear> hp){
        return hp.size()==12;
    }

    private void deathPointCalculation (String user, List<Tear> hp){
        if (!hp.isEmpty()) {
            Map<FigureColour, Integer> figuresToHits = calculateHits(hp);   //map of shooters to number of hits
            firstBlood(hp.get(0));  //player causing first blood obtains 1 extra point
            assignPoints(figuresToHits, user, hp);
        }
    }

    private void respawn(String user,String discardedPowerUpColor){
        String spawnColour=discardedPowerUpColor;
        HashMap<Point,String> mappedSpawnPoints=model.getGameMap().getMappedSpawnPoints();
        Point spawnPoint=model.userToPlayer(user).getPosition();
        for (Point p:mappedSpawnPoints.keySet()){
            if (mappedSpawnPoints.get(p).equals(spawnColour))
                spawnPoint=p;
        }
        model.userToPlayer(user).getFigure().spawn(spawnPoint);
        model.send(new MVMoveEvent("*", user, model.userToPlayer(user).getFigure().getPosition()));
    }

    @Override
    public void dispatch(DisconnectionEvent message) {
        if (!enoughActivePlayers())
            endOfMatch();
    }
}
