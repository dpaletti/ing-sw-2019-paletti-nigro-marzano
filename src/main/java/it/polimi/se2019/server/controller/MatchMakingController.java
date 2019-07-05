package it.polimi.se2019.server.controller;

import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.mv_events.MatchConfigurationEvent;
import it.polimi.se2019.commons.mv_events.MvJoinEvent;
import it.polimi.se2019.commons.mv_events.TimerEvent;
import it.polimi.se2019.commons.mv_events.UsernameDeletionEvent;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.vc_events.DisconnectionEvent;
import it.polimi.se2019.commons.vc_events.VcJoinEvent;
import it.polimi.se2019.server.model.Game;
import it.polimi.se2019.server.model.TickingTimer;
import it.polimi.se2019.server.network.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class creates the match and groups players in a single room. Upon connection of each player, their username
 * is registered and a room starts playing when at least three users are connected.
 * See {@link it.polimi.se2019.server.controller.Controller}.
 */

public class MatchMakingController extends Controller {
    private AtomicInteger playerCount = new AtomicInteger(0);
    private AtomicBoolean matchMade = new AtomicBoolean(false);
    private AtomicBoolean timerRunning = new AtomicBoolean(false);
    private List<String> usernames = new CopyOnWriteArrayList<>();
    private TickingTimer matchMakingTimer = new TickingTimer(model, this::onTimerEnd);

    public MatchMakingController(Game model, Server server, int roomNumber){
        super(model, server, roomNumber);
        usernames.add("*"); //adding wildcard so that no player can choose that username
    }

    /**
     * This method ignores the events that are not dispatched in this controller.
     * @param message Any message arriving from the view.
     */
    @Override
    public void update(VCEvent message)
    {
        if(disabled)
            return;
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //this is the only controller registered on matchMaking thus it cannot receive unsupported events
            Log.fine("Received unsupported event in matchMaking (room: " + getRoomNumber() + ")" + message);
            //throw new UnsupportedOperationException("MatchMaking controller: " + e.getMessage(), e);
        }
    }

    /**
     * Whenever a user joins the match, this method adds them to the currently forming room and starts the match if at least 3 players are connected.
     * @param message
     */
    @Override
    public void dispatch(VcJoinEvent message) {
        usernames.add(message.getUsername());
        server.addUsername(message.getUsername());
        playerCount.set(playerCount.addAndGet(1));
        model.send(new MvJoinEvent(message.getSource(), message.getUsername(), 3 - playerCount.get()));
        Log.info("Players in match making: " + playerCount);
        if (playerCount.get() == 3) {
            Log.fine("Timer started");
            matchMakingTimer.startTimer(server.getMatchMakingTimer());
            timerRunning.set(true);
        }
        if (playerCount.get() == 5) {
            timerRunning.set(false);
            matchMakingTimer.endTimer();
        }
    }

    /**
     * Handles disconnection of users. If less than 2 players are connected and active, they match is over.
     * @param disconnectionEvent
     */

    @Override
    public void dispatch(DisconnectionEvent disconnectionEvent) {
        try {
            server.kickPlayer(disconnectionEvent.getSource(), disconnectionEvent.isReconnection());
            if (usernames.remove(disconnectionEvent.getSource())) {

                playerCount.set(playerCount.decrementAndGet());
                Log.info(disconnectionEvent.getSource() + " just disconnected, players in match making; " + playerCount);
                if (playerCount.get() < 3 && timerRunning.get()) {
                    matchMakingTimer.stop();
                    timerRunning.set(false);
                    model.send(new TimerEvent("*", -1));  //negative time to go signals countdown interruption
                    Log.info("Timer stopped");
                }
            }
            model.send(new UsernameDeletionEvent("*", disconnectionEvent.getSource(), 3 -playerCount.get()));
        }catch (IllegalArgumentException e){
            Log.severe(disconnectionEvent.getSource() + " could not be found for removal");
        }

    }



    public int getPlayerCount() {
        return playerCount.get();
    }

    public boolean isTimerRunning() {
        return timerRunning.get();
    }

    public boolean isMatchMade() {
        return matchMade.get();
    }

    public List<String> getUsernames() {
        return new ArrayList<>(usernames);
    }

    /**
     * This method is called when the timer is interrupted and it closes match making initializing the set up phase.
     */

    protected void onTimerEnd() {
        Log.fine("closing match making");
        matchMade.set(true);
        List<String> actualUsernames = new ArrayList<>(usernames);
        actualUsernames.remove("*");
        closeMatchMaking(actualUsernames);
        this.disable();
        new SetUpController(model, server, getRoomNumber());
    }

    /**
     * closes match making.
     * @param usernames all the users connected to the current room and playing.
     */

    private void closeMatchMaking(List<String> usernames){
        model.setUsernames(new ArrayList<>(usernames));
        model.send(new MatchConfigurationEvent("*", model.getMapConfigs()));
    }
}
