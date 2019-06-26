package it.polimi.se2019.view;

import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.network.Client;
import it.polimi.se2019.utility.*;
import it.polimi.se2019.view.ui_events.*;
import it.polimi.se2019.view.vc_events.VcMatchConfigurationEvent;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


//TODO red skulls and disabled skulls
//TODO ask first player about preferred skull number
//TODO when sending "actions" use combotype names (in UML)
//TODO when using moveAndGrab click on your tile not to move
//TODO chosenEffectEvent: click on card section

public class ViewGUI extends View {

    private static Semaphore semControllerSync = new Semaphore(1, true);
    private List<MockPlayer> players = new ArrayList<>();
    private boolean timerGoing=false;
    private static ViewGUI instance = null;


    //TODO set board conf
    private Pair<Integer, Integer> boardConf;

    private ViewGUI(Client client){
        super(client);
    }

    public static void create(Client client){
        instance = new ViewGUI(client);
    }

    public static ViewGUI getInstance() {
        return instance;
    }

    public Pair<Integer, Integer> getBoardConf() {
        return new Pair<>(boardConf.getFirst(), boardConf.getSecond());
    }

    public void send(VCEvent message){
        notify(message);
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
            message.handle(this);
        }catch (UnsupportedOperationException e){
            Log.severe("Unsupported event in view");
            throw new UnsupportedOperationException("Error: " + e.getMessage(), e);
        }
    }

}

