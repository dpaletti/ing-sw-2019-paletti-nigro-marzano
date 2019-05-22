package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.network.Settings;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.DisconnectionEvent;
import it.polimi.se2019.view.vc_events.VcJoinEvent;
import it.polimi.se2019.view.vc_events.VcReconnectionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class MatchMakingController extends Controller {
    private AtomicInteger playerCount = new AtomicInteger(0);
    private AtomicBoolean matchMade = new AtomicBoolean(false);
    private AtomicBoolean timerRunning = new AtomicBoolean(false);
    private List<String> usernames = new CopyOnWriteArrayList<>();
    private Thread timer;
    private Dispatcher dispatcher = new Dispatcher();


    public MatchMakingController(Game model, Server server, int roomNumber){
        super(model, server, roomNumber);
        usernames.add("*"); //adding wildcard so that no player can choose that username
    }

    @Override
    public void update(VCEvent message)
    {
        try {
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            //this is the only controller registered on matchMaking thus it cannot receive unsupported events
            throw new UnsupportedOperationException("MatchMaking controller: " + e.getMessage(), e);
        }
    }

    private class Dispatcher extends VCEventDispatcher {
        @Override
        public void update(VcJoinEvent message) {
            int period = Settings.MATCH_MAKING_TIMER/10;
            usernames.add(message.getUsername());
            model.newPlayerInMatchMaking(message.getSource(), message.getUsername());

            playerCount.set(playerCount.addAndGet(1));
            Log.info("Players in match making: " + playerCount);
            if (playerCount.get() == 3) {
                Log.fine("Timer started");
                timerRunning.set(true);

               timer = new Thread(() ->{
                    int counter = 0;
                        try {
                            while(counter < Settings.MATCH_MAKING_TIMER && !Thread.currentThread().isInterrupted()){
                                model.timerTick(Settings.MATCH_MAKING_TIMER - counter);
                                counter += period;
                                sleep(period);
                            }
                            closeMatchMaking();
                        }catch (InterruptedException e){
                            Log.severe("MatchMaking timer interrupted");
                            Thread.currentThread().interrupt();
                }});
               timer.start();
            }
            if (playerCount.get() == 5) {
                timerRunning.set(false);
                timer.interrupt();
                closeMatchMaking();
            }
        }

        @Override
        public void update(DisconnectionEvent disconnectionEvent) {
            model.usernameDeletion(disconnectionEvent.getSource());
            if (usernames.remove(disconnectionEvent.getSource())) {

                playerCount.set(playerCount.decrementAndGet());
                Log.info(disconnectionEvent.getSource() + " just disconnected, players in match making; " + playerCount);
                if (playerCount.get() < 3 && timerRunning.get()) {
                    timer.interrupt();
                    timerRunning.set(false);
                    model.timerTick(-1); //negative time to go signals countdown interruption
                    Log.info("Timer stopped");
                }
            }

        }

        @Override
        public void update(VcReconnectionEvent message) {
            model.playerReconnection(message.getSource(), message.getOldToken(), true);
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
        server.addController(new MatchController(model, server, actualUsernames, getRoomNumber()), getRoomNumber());
        server.addController(new TurnController(model, server, getRoomNumber()), getRoomNumber());
        server.removeController(this, getRoomNumber());
    }

}
