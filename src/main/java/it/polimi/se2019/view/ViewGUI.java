package it.polimi.se2019.view;

import it.polimi.se2019.model.AllowedWeaponsEvent;
import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.network.Client;
import it.polimi.se2019.network.Settings;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Pair;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.gui_events.*;
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
    private static Semaphore semMatch = new Semaphore(5, true);
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
        notify(new GuiRegistrationEvent(this));
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
            semMatch.acquireUninterruptibly(5);
    }

    @Override
    public void addPlayer(String username) {
        Log.fine("notifying add player");
        players.add(new MockPlayer(username));
        notify(new GuiAddPlayer(username));
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
        semMatchMaking.acquireUninterruptibly();
        ViewGUI.this.notify(new GuiCloseMatchMaking());
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
            notify(new GuiHighlightTileEvent(p));
        }
    }

    /*@Override
    public void dispatch(DeathEvent message) {
        resetPlayer(message.getDead());
        notify(new GuiMoveFigure(usernameToPlayer(message.getDead()).getPlayerColor(), new Point(-1, -1)));
    }*/

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
            Log.fine("Update available permits: " + semMatch.availablePermits());
            semMatch.acquireUninterruptibly(5);
            notify(new GuiRegistrationEvent(this));
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

