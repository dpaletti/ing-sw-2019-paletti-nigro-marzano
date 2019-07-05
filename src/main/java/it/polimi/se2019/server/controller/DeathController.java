package it.polimi.se2019.server.controller;

import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.mv_events.EndOfMatchEvent;
import it.polimi.se2019.commons.mv_events.MVMoveEvent;
import it.polimi.se2019.commons.utility.JsonHandler;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.commons.vc_events.CalculatePointsEvent;
import it.polimi.se2019.commons.vc_events.DisconnectionEvent;
import it.polimi.se2019.commons.vc_events.SpawnEvent;
import it.polimi.se2019.commons.vc_events.VCEndOfTurnEvent;
import it.polimi.se2019.server.model.FigureColour;
import it.polimi.se2019.server.model.Game;
import it.polimi.se2019.server.model.Player;
import it.polimi.se2019.server.model.Tear;
import it.polimi.se2019.server.network.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles the death of a player and the subsequent calculation of points in all of the phases of the game:
 * during the match, at the end of it and during a final frenzy turn. See {@link it.polimi.se2019.server.controller.Controller}.
 */

public class DeathController extends AbstractDeathController{
    private boolean lastTurn = false;
    private int counter = 0;
    private TurnController turnController;

    public DeathController(Server server, int roomNumber, Game model, TurnController turnController){
        super(model, server, roomNumber);
        this.turnController = turnController;
    }

    public DeathController(Game game){
        //Simple constructor for tests
        this.model=game;
    }

    /**
     * This method ignores the events that are not dispatched in this controller.
     * @param message Any message arriving from the view.
     */
    @Override
    public void update(VCEvent message) {
        if (disabled)
            return;
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //ignore events that this controller does not support
            Log.fine("DeathController ignored " + JsonHandler.serialize(message));
        }
    }

    /**
     * This method handles a player respawning.
     * @param message
     */
    @Override
    public void dispatch(SpawnEvent message) {
        List<Tear> hp= model.getHp(message.getSource());
        if (hp.size()<11)
            return;
        deathPointCalculation(message.getSource(), hp);
        model.userToPlayer(message.getSource()).emptyHp();
        respawn(message.getSource(),message.getDiscardedPowerUpColour());
    }

    /**
     * This method handles a request to calculate points from the view. When called, it calculates points and ends the match if final frenzy mode is not activated otherwise it starts the final frenzy turn.
     * @param message
     */
    @Override
    public void dispatch(CalculatePointsEvent message) {
        for (Player p: model.getPlayers())
            deathPointCalculation(message.getSource(), model.getHp(model.playerToUser(p)));

        if (!model.isFinalFrenzy())
            endOfMatch();
        else{
            lastTurn = true;
        }
    }

    /**
     * Dispatches the end of the turn of a player.
     * @param message
     */
    @Override
    public void dispatch(VCEndOfTurnEvent message) {
        if (lastTurn) {
            counter++;
            if (counter == model.getPlayers().size()) {
                if (model.isFinalFrenzy()) {
                    finalFrenzyPointCalculation(message.getSource());
                }
                winnerPointCalculation(model.getKillshotTrack().getKillshot());
                endOfMatch();
            }
        }
    }

    /**
     * Calculates whether the player was overkilled.
     * @param hp the hps of the dead player.
     * @return whether the player was overkilled.
     */
    private boolean overkill (List<Tear> hp){
        return hp.size()==12;
    }

    /**
     * calculates points when a user dies.
     * @param user the user that died.
     * @param hp the hps of the dead player.
     */
    private void deathPointCalculation (String user, List<Tear> hp){
        if (!hp.isEmpty()) {
            Map<FigureColour, Integer> figuresToHits = calculateHits(hp);   //map of shooters to number of hits
            firstBlood(hp.get(0));  //player causing first blood obtains 1 extra point
            assignPoints(figuresToHits, user, hp);
        }
    }

    /**
     * handles a player respawning after their death.
     * @param user the user that is respawning.
     * @param discardedPowerUpColor the colour of the power up they discarded as that is the spawn tile they will respawn on.
     */

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

    /**
     * Calculates points when a player dies during final frenzy match or at the end of the turn.
     * @param user the user that died during the turn.
     */

    private void finalFrenzyPointCalculation (String user){
        Map<FigureColour, Integer> figuresToHits;   //map of shooters to number of hits
        for (Player p: model.getPlayers()){
            figuresToHits= calculateHits(p.getHp());
            assignPoints (figuresToHits, user, p.getHp());
        }
    }
}
