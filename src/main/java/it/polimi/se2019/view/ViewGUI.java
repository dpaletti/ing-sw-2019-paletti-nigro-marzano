package it.polimi.se2019.view;

import it.polimi.se2019.model.AllowedWeaponsEvent;
import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.network.Client;
import it.polimi.se2019.network.Settings;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.gui_events.*;
import javafx.application.Application;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;

//TODO red skulls and disabled skulls
//TODO ask first player about preferred skull number
//TODO when sending "actions" use combotype names (in UML)
//TODO when using moveAndGrab click on your tile not to move
//TODO chosenEffectEvent: click on card section

public class ViewGUI extends View {

    private static Semaphore sem = new Semaphore(1, true);
    private static SynchronousQueue<GuiController> guiControllers = new SynchronousQueue<>();

    public ViewGUI(Client client){
        super(client);
    }

    @Override
    public void matchMaking(List<String> usernames) {
        sem.acquireUninterruptibly();
        startControllerWatchDog();
        new Thread(() ->  Application.launch(MatchMakingGui.class)).start();
        sem.acquireUninterruptibly();
        sem.release();
        for (String username:
             usernames) {
            addPlayer(username);
        }
        Log.fine("MatchMaking showing ended");

    }

    public void startControllerWatchDog(){
        new Thread(() ->{
            try {
                while(!Thread.currentThread().isInterrupted())
                    register( guiControllers.take());
            }catch (InterruptedException e){
                Log.severe("Interrupted guiController take");
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    public void addPlayer(String username) {
        notify(new GuiAddPlayer(username));
    }

    public static void staticRegister(GuiController controller){
        try {
            guiControllers.put(controller);
            sem.release();
            Log.fine("controller registration");
        }catch (InterruptedException e){
            Log.severe("Gui Controller put interrupted");
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void dispatch(TimerEvent message) {
        if (((Integer) message.getTimeToGo()).equals(Settings.MATCH_MAKING_TIMER))
            ViewGUI.this.notify(new GuiTimerStart());
        if (message.getTimeToGo() < 0)
            ViewGUI.this.notify(new GuiTimerStop());
        ViewGUI.this.notify(new GuiTimerTick(message.getTimeToGo()));
    }

    @Override
    public void dispatch(UsernameDeletionEvent message) {
        ViewGUI.this.notify(new GuiRemovePlayer(message.getUsername()));
    }

    @Override
    public void dispatch(MatchMakingEndEvent message) {
        sem.acquireUninterruptibly();
        ViewGUI.this.notify(new GuiCloseMatchMaking());
        sem.acquireUninterruptibly();
    }

    @Override
    public void dispatch(StartFirstTurnEvent message) {
        //TODO change GUI
        //TODO send SpawnEvent
    }

    @Override
    public void dispatch(AllowedMovementsEvent message) {
        //TODO
    }

    @Override
    public void dispatch(AllowedWeaponsEvent message) {
        //TODO
    }

    @Override
    public void update(MVEvent message) {
        try {
            Log.fine("Received: " + message);
            message.handle(this);
        }catch (UnsupportedOperationException e){
            Log.severe("Unsupported event in view");
            throw new UnsupportedOperationException("Error: " + e.getMessage(), e);
        }
    }
}

