package it.polimi.se2019.client.view;

import it.polimi.se2019.client.network.Client;
import it.polimi.se2019.client.view.ui_events.*;
import it.polimi.se2019.commons.mv_events.*;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.commons.vc_events.CalculatePointsEvent;
import it.polimi.se2019.commons.vc_events.SpawnEvent;
import it.polimi.se2019.commons.vc_events.VCEndOfTurnEvent;
import it.polimi.se2019.commons.vc_events.VcMatchConfigurationEvent;
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
    private Semaphore semMove = new Semaphore(1, true);
    private List<MockPlayer> players = new ArrayList<>();
    private boolean timerGoing=false;
    private static ViewGUI instance = null;
    private Map<Point, String> pointColorSpawnMap;
    private String currentlyShownFigure;
    private boolean respawning = false;

    private Semaphore movementSem = new Semaphore(1, true);


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

    //------------------Singleton implementation-------------------//

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

    //-------------------------------------------------------------//

    //------------------Utility-------------------//
    public void send(VCEvent message){
        notify(message);
    }

    public void send(UiEvent message){
        notify(message);
    }


    public  void registerController(GuiController controller){
        register(controller);
        semControllerSync.release();
    }

    public void deregisterController(GuiController controller){
        deregister(controller);
    }

    public String getUsername(){
        return client.getUsername();
    }

    @Override
    public void dispatch(TimerEvent message) {
        //Log.fine("Time to go: " + message.getTimeToGo());
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
    public void update(MVEvent message) {
        try {
            if (!(message instanceof TimerEvent)){
                Log.fine("Received: " + message);
            }
            eventBuffer.put(message);
        }catch (UnsupportedOperationException e){
            Log.severe("Unsupported event in view");
            throw new UnsupportedOperationException("Error: " + e.getMessage(), e);
        }catch (InterruptedException e){
            Log.severe("Interrupted while adding to buffer in ViewGUI");
        }
    }

    //------------------Utility-------------------//

    //------------------MatchMaking-------------------//

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

    public void mapConfigSetup(List<String> config){
        notify(new UiMapConfigEvent(config));
    }

    @Override
    public void addPlayer(String username) {
        Log.fine("notifying add player");
        notify(new UiAddPlayer(username));
    }

    @Override
    public synchronized void dispatch(MatchConfigurationEvent message)
    {
        notify(new UiCloseMatchMaking());
    }

    @Override
    public void dispatch(UsernameDeletionEvent message) {
        notify(new UiRemovePlayer(message.getUsername()));
    }

    //------------------------------------------------//


    //------------------Setup-------------------//

    @Override
    public void dispatch(SetUpEvent message) {
        semControllerSync.release();
        for (String user : message.getUserToColour().keySet())
            players.add(new MockPlayer(user, message.getUserToColour().get(user).toLowerCase()));
        currentlyShownFigure = getPlayerOnUsername(client.getUsername()).getPlayerColor();

        semControllerSync.acquireUninterruptibly();
        notify(new UiCloseSetup());
        semControllerSync.acquireUninterruptibly();

        notify(new UiBoardInitialization(message.getWeaponSpots(), message.getLootCards(), message.getLeftConfig(), message.getRightConfig(), message.getSkulls()));
        notify(new UiSetPlayerBoard(getPlayerOnUsername(client.getUsername()).getPlayerColor()));
    }

    public void gameSetup(int skulls, boolean frenzy, String conf){
        String actualConf = conf.substring(0, 1).toUpperCase() + conf.substring(1);
        notify(new VcMatchConfigurationEvent(client.getUsername(), skulls, frenzy, actualConf));

    }

    //------------------------------------------//

    //------------------Turn Management-------------------//

    @Override
    public void dispatch(StartFirstTurnEvent message) {
        semControllerSync.acquireUninterruptibly();
        semControllerSync.acquireUninterruptibly();
        pointColorSpawnMap = message.getSpawnPoints();
        for(Point p: message.getSpawnPoints().keySet())
            notify(new UiHighlightTileEvent(p, false));
        notify(new UiPutPowerUp(message.getFirstPowerUpName()));
        notify(new UiPutPowerUp(message.getSecondPowerUpName()));
        notify(new UiSpawn());
    }

    @Override
    public void dispatch(TurnEvent message) {
        notify(new UiStartTurn());
        for(String c: message.getCombos())
            notify(new UiAvailableMove(c));
    }

    @Override
    public void dispatch(FinalFrenzyStartingEvent message) {
        notify(new UiFinalFrenzy());
    }

    @Override
    public void dispatch(PausedPlayerEvent message) {
        notify(new UiPausePlayer(message.getPausedPlayer()));
    }

    public void endTurn(){
        notify(new VCEndOfTurnEvent(client.getUsername()));
        notify(new UiTurnEnd());
    }


    @Override
    public void dispatch(UpdatePointsEvent message) {
        notify(new UiPointsEvent(message.getUsername(), message.getPoints()));
    }

    @Override
    public void dispatch(MVEndOfTurnEvent message) {
        notify(new UiTurnEnd());
    }

    @Override
    public synchronized void dispatch(SyncEvent message) {
        dispatch(new MatchConfigurationEvent(client.getUsername(), message.getConfigs()));

        dispatch(new SetUpEvent(client.getUsername(), message.getColours(), message.getWeaponSpots(), message.getLootSpots(), message.getSkulls(), message.getLeftConfig(), message.getRightConfig(), message.isFrenzy()));

        for(String paused: message.getPaused())
            dispatch(new PausedPlayerEvent(client.getUsername(), paused));

        for(String username: message.getUsernames()){

            for(String hit: message.getHp().get(username))
                dispatch(new UpdateHpEvent(client.getUsername(), username, hit));

            for(String mark: message.getMark().get(username))
                dispatch(new UpdateMarkEvent(client.getUsername(), username, mark, true));

            for(Point p: message.getFigurePosition().keySet()){
                for(String figure: message.getFigurePosition().get(p))
                    dispatch(new MVMoveEvent(client.getUsername(), figure, p));
            }

            for(String powerup: message.getPowerup())
                dispatch(new DrawnPowerUpEvent(client.getUsername(), powerup));


            for(String weapon: message.getWeapons().get(username)){
                List<String> weapons = getPlayerOnUsername(username).getWeapons();
                weapons.add(weapon);
                getPlayerOnUsername(username).setWeapons(weapons);
                if(getPlayerOnUsername(username).getPlayerColor().equals(currentlyShownFigure))
                    notify(new UiPutWeapon(weapon));
            }

            dispatch(new FinanceUpdateEvent(client.getUsername(), username, message.getFinance().get(username)));
            dispatch(new UpdatePointsEvent(client.getUsername(),username, message.getPoints().get(username)));
        }
    }

    //-----------------------------------Figure movements-----------------------------------//
    @Override
    public void dispatch(AllowedMovementsEvent message) {
        for (Point p:
                message.getAllowedPositions()) {
            notify(new UiHighlightTileEvent(p, false));
        }
    }

    @Override
    public  void dispatch(MVMoveEvent message) {
        movementSem.acquireUninterruptibly();
        notify(new UiMoveFigure(getPlayerOnUsername(message.getUsername()).getPlayerColor(), message.getFinalPosition()));

    }

    public void setPosition(String figure, Point position){
        getPlayerOnColour(figure).setPosition(position);
        movementSem.release();
    }

    public Point getPosition(String figure){
        return getPlayerOnColour(figure).getPosition();
    }

    public Point getPosition(){
        return getPlayerOnUsername(client.getUsername()).getPosition();
    }
    //--------------------------------------------------------------------------------------//
    //-----------------------------------Grabbing-------------------------------------------//

    @Override
    public void dispatch(GrabbablesEvent message) {
        movementSem.acquireUninterruptibly();
        movementSem.release();
        if(isSpawn(usernameToPlayer(client.getUsername()).getPosition()) != null)
            notify(new UiGrabWeapon(pointColorSpawnMap.get(isSpawn(usernameToPlayer(client.getUsername()).getPosition()))));
        else
            notify(new UiGrabLoot(getPlayerOnUsername(client.getUsername()).getPosition()));
    }

    @Override
    public void dispatch(GrabbedWeaponEvent message) {
        List<String> weapons = getPlayerOnUsername(message.getUser()).getWeapons();
        weapons.add(message.getWeapon());
        getPlayerOnUsername(message.getUser()).setWeapons(weapons);
        notify(new UiRemoveWeaponFromSpot(message.getWeapon()));

    }

    @Override
    public void dispatch(DrawnPowerUpEvent message) {
        notify(new UiPutPowerUp(message.getDrawn()));
    }

    @Override
    public void dispatch(GrabbedLootCardEvent message) {
        notify(new UiGrabbedLoot(message.getGrabbedLootCard()));
    }

    @Override
    public void dispatch(BoardRefreshEvent message) {
        notify(new UiBoardRefresh(message.getWeaponSpots(), message.getLootCards()));
    }

    //------------------------------------MockPlayer Manipulation--------------------------------------------//

    @Override
    public void dispatch(UpdateHpEvent message) {
        MockPlayer attacked = getPlayerOnUsername(message.getAttacked());
        MockPlayer attacker = getPlayerOnUsername(message.getAttacker());
        List<String> newHp = attacked.getHp();
        newHp.add(attacker.getPlayerColor().toLowerCase());

        if(attacked.getPlayerColor().equals(currentlyShownFigure)){
            notify(new UiAddDamage(attacker.getPlayerColor().toLowerCase(), attacked.getHp().size(), false));
        }
        attacked.setHp(newHp);
    }

    @Override
    public void dispatch(UpdateMarkEvent message) {
        MockPlayer marked = getPlayerOnUsername(message.getMarked());
        MockPlayer marker = getPlayerOnUsername(message.getMarker());
        List<Integer> marksToTake =  new ArrayList<>();
        List<String> newMarks = marked.getMark();
        if(!message.isAdding()){
            int i = 0;
            for (String colour : marked.getMark()) {
                if (colour.equalsIgnoreCase(marker.getPlayerColor())) {
                    marksToTake.add(i);
                    newMarks.remove(colour);
                }
                i++;
            }
            if(marked.getPlayerColor().equals(currentlyShownFigure))
                notify(new UiRemoveMarks(marksToTake));
        }else{
            newMarks.add(marked.getPlayerColor().toLowerCase());

            if(marked.getPlayerColor().equals(currentlyShownFigure))
                notify(new UiAddDamage(marker.getPlayerColor(), marked.getMark().size(), true));
        }
        marked.setMark(newMarks);
    }

    public List<String> getMarks (){
        //return marks of showed player
        return getPlayerOnColour(currentlyShownFigure).getMark();
    }

    @Override
    public void dispatch(FinanceUpdateEvent message) {
        if(getPlayerOnUsername(message.getUsername()).getPlayerColor().equals(currentlyShownFigure))
            notify(new UiAmmoUpdate(message.getUpdatedAmmos()));
        getPlayerOnUsername(message.getUsername()).setAmmos(message.getUpdatedAmmos());
    }

    @Override
    public void dispatch(MVDeathEvent message) {
        if(message.isMatchOver()){
            if(client.getUsername().equals(message.getDead())){
                notify(new CalculatePointsEvent(client.getUsername()));
                return;
            }
        }
        notify(new UiMoveFigure(getPlayerOnUsername(message.getDead()).getPlayerColor(), new Point(-1, -1)));
        notify(new UiAddKillOnSkulls(message.isOverkill(), getPlayerOnUsername(message.getKiller()).getPlayerColor()));
        if(getPlayerOnUsername(message.getDead()).getPlayerColor().equalsIgnoreCase(currentlyShownFigure))
            notify(new UiHpReset());
        getPlayerOnUsername(message.getDead()).setHp(new ArrayList<>());
    }

    public void dispatch(MVRespawnEvent message){
        respawning = true;
        dispatch(new UiPutPowerUp(message.getDrawnPowerUpName()));
        dispatch(new UiRespawnEvent());
    }

    //---------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------//

    //----------------------------------Shooting---------------------------------------------//

    @Override
    public void dispatch(AllowedWeaponsEvent message) {
        notify(new UiActivateWeapons());
    }

    @Override
    public void dispatch(PossibleEffectsEvent message) {
        notify(new UiActivateWeaponEffects(message.getName(), message.getEffects()));
    }

    @Override
    public void dispatch(PartialSelectionEvent message) {
        if((message.getTargetPlayers() == null && message.getTargetTiles() == null) ||
                (message.getTargetPlayers() != null && message.getTargetTiles() != null))
            throw new IllegalArgumentException("Could not handle targeting, wrong event format");

        notify(new UiSkip(message.isSkippable()));

        if(message.getTargetTiles() != null) {
            for (Point tile : message.getTargetTiles())
                notify(new UiHighlightTileEvent(tile, true));
        }else{
            for(String figure: message.getTargetPlayers())
                notify(new UiHighlightPlayer(getPlayerOnUsername(figure).getPlayerColor()));
        }

    }


    @Override
    public void dispatch(ReloadableWeaponsEvent message) {
        notify(new UiReloading(message.getPriceMap()));
    }

    @Override
    public void dispatch(MVCardEndEvent message) {
        if(message.isWeapon())
            notify(new UiWeaponEnd());
        else
            notify(new UiPowerUpEnd());
    }

    //---------------------------------------------------------------------------------------//

    //-----------------------------------PowerUp----------------------------------------------//

    @Override
    public void dispatch(UsablePowerUpEvent message) {
        notify(new UiAvailablePowerup(message.getUsablePowerUp()));
    }

    @Override
    public void dispatch(DisablePowerUpEvent message) {
        notify(new UiDisablePowerUpEvent(message.getPowerUp()));
    }

    @Override
    public void dispatch(MVChooseAmmoToPayEvent message) {
        notify(new UiChooseAmmoToPay(message.getAvailableAmmos()));
    }

    @Override
    public void dispatch(MVSellPowerUpEvent message) {
        notify(new UiSellPowerups(message.getPowerUpsToSell(), message.getColoursToMissing()));
    }
    //----------------------------------------------------------------------------------------//

    //-------------------------------------Utility methods-------------------------------------//


    public boolean isRespawning() {
        return respawning;
    }

    public void setRespawning(boolean respawning) {
        this.respawning = respawning;
    }

    public List<String> getWeapons(){
        return getPlayerOnColour(currentlyShownFigure).getWeapons();
    }

    public List<String> getHp(){
        return getPlayerOnColour(currentlyShownFigure).getHp();
    }

    public List<String> getAmmos(){
        return getPlayerOnColour(currentlyShownFigure).getAmmos();
    }

    public List<String> getHeadPlayerAmmos(){
        return getPlayerOnUsername(client.getUsername()).getAmmos();

    }

    public MockPlayer getPlayerOnColour(String figure){
        for (MockPlayer p: players){
            if(p.getPlayerColor().equalsIgnoreCase(figure))
                return p;
        }
        throw new IllegalArgumentException("Could not find player with figure colour: " + figure);
    }


    public String getColour(){
        return getPlayerOnUsername(client.getUsername()).getPlayerColor();
    }



    private MockPlayer getPlayerOnUsername(String username){
        for(MockPlayer m: players){
            if(m.getUsername().equalsIgnoreCase(username))
                return m;
        }
        throw new IllegalArgumentException("Could not find any player with username: " + username);
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

        notify(new UiResetMap());
        notify(new SpawnEvent(client.getUsername(),colour, powerupToKeep));
    }


    private Point isSpawn(Point p){
        for(Point spawn: pointColorSpawnMap.keySet()){
            if(spawn.getX() == p.getX() && spawn.getY() == p.getY())
                return spawn;
        }
        return null;
    }

    private void resetPlayer(String username){
        MockPlayer player = usernameToPlayer(username);
        player.setHp(new ArrayList<>());
        player.setMark(new ArrayList<>());
    }

    private MockPlayer usernameToPlayer(String username){
        for (MockPlayer m:
             players) {
            if(m.getUsername().equals(username))
                return m;
        }
        throw new IllegalStateException("Could not find player with given username");
    }

    public void setCurrentlyShownFigure(String currentlyShownFigure) {
        this.currentlyShownFigure = currentlyShownFigure;
    }

}

