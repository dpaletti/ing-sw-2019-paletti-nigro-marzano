package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.network.Settings;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.vc_events.JoinEvent;
import it.polimi.se2019.view.vc_events.DisconnectionEvent;
import it.polimi.se2019.view.VCEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchMakingController extends Controller {
    private AtomicInteger playerCount = new AtomicInteger(0);
    private AtomicBoolean matchMade = new AtomicBoolean(false);
    private AtomicBoolean timerRunning = new AtomicBoolean(false);
    private List<String> usernames = new CopyOnWriteArrayList<>();
    private Timer timer = new Timer();
    private Dispatcher dispatcher = new Dispatcher();

    public MatchMakingController(Game model, Server server){
        super(model, server);
        usernames.add("*"); //adding wildcard so that no player can choose that username
    }

    @Override
    public void update(VCEvent message)
    {
        try {
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException("MatchMaking controller: " + e.getMessage(), e);
        }
    }

    private class Dispatcher extends VCEventDispatcher{
        @Override
        public void update(JoinEvent message) {
            Log.fine("Here I am");
            //JoinEvents arriving here can't be re-connections so the token is always null
            if(usernames.contains(message.getUsername())) {
                Log.fine("Username is not valid");
                model.invalidUsername(message.getSource(), new ArrayList<>(usernames));
                return;
            }

            model.validUsername(message.getSource(), message.getUsername());
            usernames.add(message.getUsername());

            playerCount.set(playerCount.addAndGet(1));
            Log.info("Players in match making: " + playerCount);
            if (playerCount.get() == 3) {
                Log.fine("Timer started");
                timerRunning.set(true);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        closeMatchMaking();
                    }
                }, Settings.MATCH_MAKING_TIMER); //time in milliseconds
            }
            //TODO make time interval configurable
            if (playerCount.get() == 5) {
                timerRunning.set(false);
                timer.cancel();
                timer.purge();
                closeMatchMaking();
            }
        }

        @Override
        public void update(DisconnectionEvent disconnectionEvent){
            usernames.remove(disconnectionEvent.getSource());
            model.usernameDeletion(disconnectionEvent.getSource());

            playerCount.set(playerCount.decrementAndGet());
            Log.info(disconnectionEvent.getSource() + " just disconnected, players in match making; " + playerCount);
            if(playerCount.get() < 3 && timerRunning.get()){
                timerRunning.set(false);
                Log.info("Timer stopped");
            }

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

    private void closeMatchMaking(){
        Log.fine("closing match making");
        matchMade.set(true);
        List<String> actualUsernames = new ArrayList<>(usernames);
        actualUsernames.remove("*");
        model.closeMatchMaking(actualUsernames);
        server.addController(new MatchController(model, server));
        server.removeController(this);

    }

}
