package it.polimi.se2019.view;

import it.polimi.se2019.model.mv_events.MatchMakingEndEvent;
import it.polimi.se2019.model.mv_events.TimerEvent;
import it.polimi.se2019.model.mv_events.UsernameDeletionEvent;
import it.polimi.se2019.network.Client;
import it.polimi.se2019.network.Settings;
import it.polimi.se2019.utility.Log;
import javafx.application.Application;

import java.util.List;
import java.util.concurrent.Semaphore;


public class ViewGUI extends View{


    private static MatchMakingControllerGui matchMakingControllerGui;
    private Dispatcher dispatcher = new Dispatcher();
    private Thread gui;
    private static Semaphore sem = new Semaphore(1, true);


    public ViewGUI(Client client){
        super(client);
    }

    @Override
    public void matchMaking(List<String> usernames) {

        try {
            sem.acquire();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        gui = new Thread(() ->  Application.launch(MatchMakingGui.class));
        gui.start();
        try {
            sem.acquire();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

        for (String username:
             usernames) {
            matchMakingControllerGui.addPlayer(username);
        }
    }

    @Override
    public void addPlayer(String username) {
        matchMakingControllerGui.addPlayer(username);
    }

    public static void setMatchMakingControllerGui(MatchMakingControllerGui controller){
        matchMakingControllerGui = controller;
        sem.release();
    }

    private class Dispatcher extends CommonDispatcher {

        @Override
        public void update(TimerEvent message) {
            Log.fine("Time to go: " + message.getTimeToGo());
            if(((Integer) message.getTimeToGo()).equals(Settings.MATCH_MAKING_TIMER))
                matchMakingControllerGui.startTimer();
            if(message.getTimeToGo() < 0)
                matchMakingControllerGui.stopTimer();
            matchMakingControllerGui.timerTick(message.getTimeToGo());
        }

        @Override
        public void update(UsernameDeletionEvent message) {
            matchMakingControllerGui.removePlayer(message.getUsername());
        }

        @Override
        public void update(MatchMakingEndEvent message){
            //TODO
        }
    }

    @Override
    public void update(MVEvent message) {
        try {
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            Log.severe("Unsupported event in view");
            throw new UnsupportedOperationException("Error: " + e.getMessage(), e);
        }
    }
}
