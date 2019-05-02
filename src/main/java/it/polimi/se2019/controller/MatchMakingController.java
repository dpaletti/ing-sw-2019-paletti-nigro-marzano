package it.polimi.se2019.controller;

import it.polimi.se2019.network.VirtualView;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.MatchMakingEntranceRequestEvent;
import it.polimi.se2019.view.DisconnectionEvent;
import it.polimi.se2019.view.VCEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchMakingController extends Controller {
    private AtomicInteger playerCount = new AtomicInteger();
    private AtomicBoolean matchMade = new AtomicBoolean();
    private AtomicBoolean timerRunning = new AtomicBoolean();
    private Timer timer = new Timer();

    public MatchMakingController(VirtualView virtualView){
        super(virtualView);
        playerCount.set(0);
        matchMade.set(false);
        timerRunning.set(false);
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

    private void closeMatchMaking(){
        //TODO game starting mechanics to define
        //now players need to be added in the model
        //this controller does not need to stay among observers any longer
        //a new MatchController needs to be instantiated and used
        Log.fine("closing match making");
        matchMade.set(true);
    }

    @Override
    public void update(VCEvent message){
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            Log.severe("Unsupported event type: " + e.getMessage());
        }
    }

    public void update(MatchMakingEntranceRequestEvent message){
        virtualView.addPlayer(message);
        playerCount.set(playerCount.addAndGet(1));
        Log.info("Players in match making: " + playerCount);
        if(playerCount.get() == 3){
            Log.fine("Timer started");
            timerRunning.set(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    closeMatchMaking();
                }
            }, 1000); //time in milliseconds
        }
        //TODO make time interval configurable
        if(playerCount.get() == 5){
            timerRunning.set(false);
            timer.cancel();
            timer.purge();
            closeMatchMaking();
        }
    }

    public void update(DisconnectionEvent disconnectionEvent){
        virtualView.timeOut(disconnectionEvent.getSource());
        playerCount.set(playerCount.decrementAndGet());
        if(playerCount.get() < 0)
            throw new IllegalArgumentException();
        Log.info("One player just disconnected, players in match making; " + playerCount);
        if(playerCount.get() < 3 && timerRunning.get()){
            timerRunning.set(false);
            Log.info("Timer stopped");
        }

    }

}
