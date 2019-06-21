package it.polimi.se2019.view;

import it.polimi.se2019.model.mv_events.AllowedWeaponsEvent;
import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.network.Client;
import it.polimi.se2019.network.Settings;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Pair;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.gui_events.*;
import it.polimi.se2019.view.vc_events.VcMatchConfigurationEvent;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;


//TODO red skulls and disabled skulls
//TODO ask first player about preferred skull number
//TODO when sending "actions" use combotype names (in UML)
//TODO when using moveAndGrab click on your tile not to move
//TODO chosenEffectEvent: click on card section

public class ViewGUI extends View {

    private static Semaphore semMatchMaking = new Semaphore(1, true);
    private static Semaphore semMatch = new Semaphore(1, true);
    boolean matchInitialization = false;
    private static SynchronousQueue<GuiController> guiControllers = new SynchronousQueue<>();
    private List<MockPlayer> players = new ArrayList<>();


    //TODO set board conf
    private Pair<Integer, Integer> boardConf;

    public ViewGUI(Client client){
        super(client);
    }

    public Pair<Integer, Integer> getBoardConf() {
        return new Pair<>(boardConf.getFirst(), boardConf.getSecond());
    }

    @Override
    public void matchMaking(List<String> usernames) {
        semMatchMaking.acquireUninterruptibly();
        startControllerWatchDog();
        new Thread(() ->  Application.launch(MatchMakingGui.class)).start();
        semMatchMaking.acquireUninterruptibly();
        semMatchMaking.release();
        notify(new UiRegistrationEvent(this));
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

    public void closeMatchMaking(){
        Log.fine("Closing matchMaking, semaphore: " + semMatch.availablePermits());
            matchInitialization = true;
            semMatch.acquireUninterruptibly();
    }

    public void gameSetup(int skulls, boolean frenzy, String conf){
        notify(new VcMatchConfigurationEvent(client.getUsername(), skulls, frenzy, conf));
    }

    @Override
    public void addPlayer(String username) {
        Log.fine("notifying add player");
        players.add(new MockPlayer(username));
        notify(new UiAddPlayer(username));
    }

    public static void staticRegister(GuiController controller){
        try {

            guiControllers.put(controller);
            semMatchMaking.release();
            semMatch.release();
            Log.fine("controller registration");
        }catch (InterruptedException e){
            Log.severe("Gui Controller put interrupted");
            Thread.currentThread().interrupt();
        }
    }


    @Override
    public void dispatch(TimerEvent message) {
        if (((Integer) message.getTimeToGo()).equals(Settings.MATCH_MAKING_TIMER))
            ViewGUI.this.notify(new UiTimerStart());
        if (message.getTimeToGo() < 0)
            ViewGUI.this.notify(new UiTimerStop());
        ViewGUI.this.notify(new UiTimerTick(message.getTimeToGo()));
    }

    @Override
    public void dispatch(UsernameDeletionEvent message) {
        ViewGUI.this.notify(new UiRemovePlayer(message.getUsername()));
    }

    @Override
    public void dispatch(MatchMakingEndEvent message) {
        semMatchMaking.acquireUninterruptibly();
        ViewGUI.this.notify(new UiCloseMatchMaking());
        semMatchMaking.acquireUninterruptibly();
    }

    @Override
    public void dispatch(StartFirstTurnEvent message) {
        //TODO change GUI
        //TODO send SpawnEvent
    }

    @Override
    public void dispatch(AllowedMovementsEvent message) {
        for (Point p:
                message.getAllowedPositions()) {
            notify(new UiHighlightTileEvent(p));
        }
    }

    @Override
    public void dispatch(MatchConfigurationEvent message) {
        notify(new UiMatchSetup(message.getConfigurations()));
    }

    private void resetPlayer(String username){
        MockPlayer player = usernameToPlayer(username);
        player.setHp(new ArrayList<>());
        player.setMark(new ArrayList<>());
        player.decreaseValue();
    }

    private MockPlayer usernameToPlayer(String username){
        for (MockPlayer m:
             players) {
            if(m.getUsername().equals(username))
                return m;
        }
        throw new IllegalStateException("Could not find player with given username");
    }

    @Override
    public void dispatch(AllowedWeaponsEvent message) {
        //TODO
    }


    @Override
    public void update(MVEvent message) {
        if(matchInitialization){
            semMatch.acquireUninterruptibly();
            notify(new UiRegistrationEvent(this));
            Log.fine("Semaphore released");
            matchInitialization = false;
        }
        try {
            Log.fine("Received: " + message);
            message.handle(this);
        }catch (UnsupportedOperationException e){
            Log.severe("Unsupported event in view");
            throw new UnsupportedOperationException("Error: " + e.getMessage(), e);
        }
    }

    public boolean isMatchInitialization() {
        return matchInitialization;
    }
}

