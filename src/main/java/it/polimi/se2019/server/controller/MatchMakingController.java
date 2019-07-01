package it.polimi.se2019.server.controller;

import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.mv_events.MatchConfigurationEvent;
import it.polimi.se2019.commons.mv_events.MvJoinEvent;
import it.polimi.se2019.commons.mv_events.TimerEvent;
import it.polimi.se2019.commons.mv_events.UsernameDeletionEvent;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.vc_events.DisconnectionEvent;
import it.polimi.se2019.commons.vc_events.VcJoinEvent;
import it.polimi.se2019.commons.vc_events.VcReconnectionEvent;
import it.polimi.se2019.server.model.Game;
import it.polimi.se2019.server.model.TickingTimer;
import it.polimi.se2019.server.network.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Override
    public void update(VCEvent message)
    {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //this is the only controller registered on matchMaking thus it cannot receive unsupported events
            Log.severe("Received unsupported event " + message);
            throw new UnsupportedOperationException("MatchMaking controller: " + e.getMessage(), e);
        }
    }

    @Override
    public void dispatch(VcJoinEvent message) {
        usernames.add(message.getUsername());
        model.send(new MvJoinEvent(message.getSource(), message.getUsername()));
        server.addUsername(message.getUsername());

        playerCount.set(playerCount.addAndGet(1));
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

    @Override
    public void dispatch(DisconnectionEvent disconnectionEvent) {
        try {
            server.kickPlayer(disconnectionEvent.getSource());
            model.send(new UsernameDeletionEvent("*", disconnectionEvent.getSource()));
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
        }catch (IllegalArgumentException e){
            Log.severe("Player that does not belong to any virtualView trying to disconnect, ignoring");
        }

    }

    @Override
    public void dispatch(VcReconnectionEvent message) {
        server.handleReconnection(message.getSource(), message.getOldToken()); //source can be either a username or a token
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

    protected void onTimerEnd() {
        Log.fine("closing match making");
        matchMade.set(true);
        List<String> actualUsernames = new ArrayList<>(usernames);
        actualUsernames.remove("*");
        closeMatchMaking(actualUsernames);
        new SetUpController(model, server, getRoomNumber());
        server.removeController(this, getRoomNumber());
    }

    private void closeMatchMaking(List<String> usernames){
        model.setUsernames(new ArrayList<>(usernames));
        model.send(new MatchConfigurationEvent("*", model.getMapConfigs()));
    }
}
