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
import java.util.concurrent.SynchronousQueue;


public class ViewGUI extends View{


    private static GuiControllerMatchMaking guiControllerMatchMaking;
    private Dispatcher dispatcher = new Dispatcher();
    private static Semaphore sem = new Semaphore(1, true);
    private static SynchronousQueue<GuiController> guiControllers = new SynchronousQueue<>();

    public ViewGUI(Client client){
        super(client);
    }

    @Override
    public void matchMaking(List<String> usernames) {
        sem.acquireUninterruptibly();
        new Thread(() ->  Application.launch(MatchMakingGui.class)).start();
        sem.acquireUninterruptibly();
        sem.release();
        for (String username:
             usernames) {
            guiControllerMatchMaking.addPlayer(username);
        }
        startControllerWatchDog();

    }

    public void startControllerWatchDog(){
        new Thread(() ->{
            try {
                while(Thread.currentThread().isInterrupted())
                    register(guiControllers.take());
            }catch (InterruptedException e){
                Log.severe("Interrupted guiController take");
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    public void addPlayer(String username) {
        guiControllerMatchMaking.addPlayer(username);
    }

    public static void staticRegister(GuiController controller){
        try {
            guiControllers.put(controller);
            sem.release();
        }catch (InterruptedException e){
            Log.severe("Gui Controller put interrupted");
            Thread.currentThread().interrupt();
        }
    }

    private class Dispatcher extends CommonDispatcher {

        @Override
        public void update(TimerEvent message) {
            if (((Integer) message.getTimeToGo()).equals(Settings.MATCH_MAKING_TIMER))
                guiControllerMatchMaking.startTimer();
            if (message.getTimeToGo() < 0)
                guiControllerMatchMaking.stopTimer();
            guiControllerMatchMaking.timerTick(message.getTimeToGo());
        }

        @Override
        public void update(UsernameDeletionEvent message) {
            guiControllerMatchMaking.removePlayer(message.getUsername());
        }

        @Override
        public void update(MatchMakingEndEvent message) {
            Log.fine("Acquiring semaphore");
            sem.acquireUninterruptibly();
            Log.fine("Closing matchMaking");
            guiControllerMatchMaking.endMatchMaking();
            sem.acquireUninterruptibly();
        }
    }

    @Override
    public void update(MVEvent message) {
        try {
            Log.fine("Received: " + message);
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            Log.severe("Unsupported event in view");
            throw new UnsupportedOperationException("Error: " + e.getMessage(), e);
        }
    }
}

