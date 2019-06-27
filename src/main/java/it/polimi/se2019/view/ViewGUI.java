package it.polimi.se2019.view;

import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.network.Client;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.ui_events.*;
import it.polimi.se2019.view.vc_events.PowerUpUsageEvent;
import it.polimi.se2019.view.vc_events.SpawnEvent;
import it.polimi.se2019.view.vc_events.VcMatchConfigurationEvent;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;


//TODO red skulls and disabled skulls
//TODO ask first player about preferred skull number
//TODO when sending "actions" use combotype names (in UML)
//TODO when using moveAndGrab click on your tile not to move
//TODO chosenEffectEvent: click on card section

public class ViewGUI extends View {

    private ArrayBlockingQueue<MVEvent> eventBuffer = new ArrayBlockingQueue<>(100);
    private static Semaphore semControllerSync = new Semaphore(1, true);
    private List<MockPlayer> players = new ArrayList<>();
    private boolean timerGoing=false;
    private static ViewGUI instance = null;
    private Map<Point, String> pointColorSpawnMap;


    private class EventListener implements Runnable{
        @Override
        public void run() {
            try {
                while(!Thread.currentThread().isInterrupted())
                    (eventBuffer.take()).handle(ViewGUI.this);

            }catch (InterruptedException e){
                Log.severe("Could not take event from buffer in View");
            }

        }
    }

    private ViewGUI(Client client){
        super(client);
        new Thread(new EventListener()).start();
    }

    public static void create(Client client){
        instance = new ViewGUI(client);
    }

    public static ViewGUI getInstance() {
        return instance;
    }

    public void send(VCEvent message){
        notify(message);
    }

    public void fourthPowerUp(String powerUp){
        //TODO
    }

    public void usePowerUp(String powerUp){
        notify(new PowerUpUsageEvent(client.getUsername(), powerUp));
    }

    @Override
    public void dispatch(TurnEvent message) {
        for(String c: message.getCombos())
            notify(new UiAvailableMove(c));
    }

    @Override
    public void dispatch(UsablePowerUpEvent message) {
        new UiAvailablePowerup(message.getUsablePowerUp());
    }

    @Override
    public void matchMaking(List<String> usernames, List<String> configs) {
        semControllerSync.acquireUninterruptibly();
        new Thread(() ->  Application.launch(App.class)).start();
        Log.fine("Try sem re-acquisition");
        semControllerSync.acquireUninterruptibly();
        mapConfigSetup(configs);
        for (String username:
             usernames) {
            addPlayer(username);
        }
        Log.fine("MatchMaking showing ended");

    }

    private MockPlayer getPlayerOnColour(String figure){
        for (MockPlayer p: players){
            if(p.getPlayerColor().equalsIgnoreCase(figure))
                return p;
        }
        throw new IllegalArgumentException("Could not find player with figure colour: " + figure);
    }

    public String getColour(){
        return getPlayerOnUsername(client.getUsername()).getPlayerColor();
    }

    public void useCombo(){

    }

    public void lockPlayers(List<String> figuresToLock){
        notify(new UiLockPlayers(figuresToLock));
    }

    public void unlockPlayers(){
        notify(new UiUnlockPlayers());
    }

    public void showPlayers(List<String> figuresToShow){
        notify(new UiShowPlayers(figuresToShow));
    }

    public void hidePlayers(List<String> figuresToHide){
        notify(new UiHidePlayers(figuresToHide));
    }

    public Point getPosition(String figure){
        return getPlayerOnColour(figure).getPosition();
    }

    public void setPosition(String figure, Point position){
        getPlayerOnColour(figure).setPosition(position);
    }

    public void contextSwitch(String figure){
        notify(new UiContextSwitch(figure));
    }

    private MockPlayer getPlayerOnUsername(String username){
        for(MockPlayer m: players){
            if(m.getUsername().equalsIgnoreCase(username))
                return m;
        }
        throw new IllegalArgumentException("Could not find any player with username: " + username);
    }

    public void gameSetup(int skulls, boolean frenzy, String conf){
        String actualConf = conf.substring(0, 1).toUpperCase() + conf.substring(1);
        notify(new VcMatchConfigurationEvent(client.getUsername(), skulls, frenzy, actualConf));

    }

    public void show(String weapon){
        notify(new UiShowWeapon(weapon));
    }

    public void hide(String weapon){
        notify(new UiHideWeapon(weapon));
    }

    public void mapConfigSetup(List<String> config){
        notify(new UiMapConfigEvent(config));
    }

    @Override
    public void addPlayer(String username) {
        Log.fine("notifying add player");
        notify(new UiAddPlayer(username));
    }

    public  void registerController(GuiController controller){
        register(controller);
        semControllerSync.release();
    }

    public void deregisterController(GuiController controller){
        deregister(controller);
    }

    public Point spawnPointOnColour(String colour){
        for(Map.Entry<Point, String> m : pointColorSpawnMap.entrySet()){
            if(m.getValue().equalsIgnoreCase(colour))
                return m.getKey();
        }
        throw new IllegalArgumentException("Could not found spawn point given colour: " + colour);
    }


    public void chooseSpawn(String powerupToDiscard, String powerupToKeep){
        Point p = null;
        String colour = null;
        if(powerupToDiscard.contains("Blue")) {
            p = spawnPointOnColour("blue");
            colour = "BLUE";
        }
        else if(powerupToDiscard.contains("Red")) {
            p = spawnPointOnColour("red");
            colour = "RED";
        }
        else if (powerupToDiscard.contains("Yellow")) {
            p = spawnPointOnColour("yellow");
            colour = "YELLOW";
        }
        if(p == null)
            throw new IllegalArgumentException(powerupToDiscard + "could not be parsed to get spawn point");

        notify(new SpawnEvent(client.getUsername(),colour, powerupToKeep));
    }

    @Override
    public void dispatch(MVMoveEvent message) {
        notify(new UiMoveFigure(getPlayerOnUsername(message.getUsername()).getPlayerColor(), message.getFinalPosition()));

    }

    @Override
    public void dispatch(TimerEvent message) {
        Log.fine("Time to go: " + message.getTimeToGo());
        if (!timerGoing) {
            notify(new UiTimerStart(message.getTimeToGo()));
            timerGoing = true;
        }
        if (message.getTimeToGo() <= 1000) {
            notify(new UiTimerStop());
            timerGoing = false;
        }
        notify(new UiTimerTick(message.getTimeToGo()));
    }

    @Override
    public void dispatch(SetUpEvent message) {
        semControllerSync.release();
        for (String user : message.getUserToColour().keySet())
            players.add(new MockPlayer(user, message.getUserToColour().get(user)));
        semControllerSync.acquireUninterruptibly();
        notify(new UiCloseSetup());
        semControllerSync.acquireUninterruptibly();
        notify(new UiBoardInitialization(message.getWeaponSpots(), message.getLootCards(), message.getLeftConfig(), message.getRightConfig(), message.getSkulls()));
        notify(new UiSetPlayerBoard(getPlayerOnUsername(client.getUsername()).getPlayerColor()));
    }

    @Override
    public void dispatch(UsernameDeletionEvent message) {
        notify(new UiRemovePlayer(message.getUsername()));
    }


    @Override
    public void dispatch(StartFirstTurnEvent message) {
        semControllerSync.acquireUninterruptibly();
        semControllerSync.acquireUninterruptibly();
        pointColorSpawnMap = message.getSpawnPoints();
        for(Point p: message.getSpawnPoints().keySet())
            notify(new UiHighlightTileEvent(p));
        notify(new UiPutPowerUp(message.getFirstPowerUpName()));
        notify(new UiPutPowerUp(message.getSecondPowerUpName()));
        notify(new UiSpawn());
    }

    @Override
    public void dispatch(AllowedMovementsEvent message) {
        for (Point p:
                message.getAllowedPositions()) {
            notify(new UiHighlightTileEvent(p));
        }
    }

    @Override
    public synchronized void dispatch(MatchConfigurationEvent message)
    {
        notify(new UiCloseMatchMaking());
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
        try {
            Log.fine("Received: " + message);
            eventBuffer.put(message);
        }catch (UnsupportedOperationException e){
            Log.severe("Unsupported event in view");
            throw new UnsupportedOperationException("Error: " + e.getMessage(), e);
        }catch (InterruptedException e){
            Log.severe("Interrupted while adding to buffer in ViewGUI");
        }
    }

}

