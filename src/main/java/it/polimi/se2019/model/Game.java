package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.utility.BiSet;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Pair;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game extends Observable<MVEvent> {
    private GameMap gameMap;
    private KillshotTrack killshotTrack;
    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck ammoDeck;
    private static Game instance;
    private List<Player> players;
    private List<Turn> turns;
    private BiSet<FigureColour, String> userLookup = new BiSet<>();
    private Map<String, Effect> effectMap;

    // TODO Mapping between figures and usernames coming from controller

    public Game(){
        //TODO implement
        //keep the line below!!!!111111!!!1!1!!!
        observers = new ArrayList<>();
    }

    public void newPlayerInMatchMaking(String token, String username){
        notify(new MvJoinEvent(token, username));
    }

    public void playerReconnection(String token, String oldToken, boolean isMatchMaking){
        notify(new MvReconnectionEvent(token, oldToken, isMatchMaking));
    }

    public void usernameDeletion(String username){
        notify(new UsernameDeletionEvent(username));
    }

    public void closeMatchMaking(List<String> usernames){
        int colourCounter=0;
        for (String userCounter: usernames){
            userLookup.add(new Pair<>(FigureColour.values()[colourCounter], userCounter));
            colourCounter++;
        }
        // '*' is a wildcard, it means that the event goes to everybody
        notify(new MatchMakingEndEvent("*"));
    }

    public void startMatch(){
        //TODO implement
    }

    public void pausePlayer(String username){
        //When a player disconnects or times out while playing needs to be paused
        //upon reconnection it will be un-paused
        //throws exception when pausing an already paused player
        //TODO implement
    }

    public void unpausePlayer(String username){
        //unpauses paused player
        //throws exception when unpausing an already unpaused player
        //TODO implement
    }

    public static Game getInstance() {
        return instance;
    }

    public Deck getAmmoDeck() { return ammoDeck; }

    public Deck getPowerUpDeck() {
        return powerUpDeck;
    }

    public Deck getWeaponDeck() {
        return weaponDeck;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public KillshotTrack getKillshotTrack() {
        return killshotTrack;
    }

    public List<Player> getPlayers() { return players; }

    public List<Turn> getTurns() { return turns; }

    public Map<String, Effect> getEffectMap() {
        return effectMap;
    }

    public BiSet<FigureColour, String> getUserLookup() {
        return userLookup;
    }

    public void setAmmoDeck(Deck ammoDeck) {
        this.ammoDeck = ammoDeck;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public void setKillshotTrack(KillshotTrack killshotTrack) {
        this.killshotTrack = killshotTrack;
    }

    public void setPowerUpDeck(Deck powerUpDeck) {
        this.powerUpDeck = powerUpDeck;
    }

    public void setWeaponDeck(Deck weaponDeck) {
        this.weaponDeck = weaponDeck;
    }

    public static void setInstance(Game instance) { Game.instance = instance; }

    public void setPlayers(List<Player> players) { this.players = players; }

    public void setTurns(List<Turn> turns) { this.turns = turns; }

    public void sendMessage (MVEvent message){
        notify(message);
    }

    public Player colourToPlayer (FigureColour figureColour){
        Player playerOfSelectedColour= new Player();
        for (Player playerCounter: players){
            if (playerCounter.getFigure().getColour().equals(figureColour)){
                playerOfSelectedColour=playerCounter;
                break;
            }
        }
        return playerOfSelectedColour;
    }

    public String colourToUser (FigureColour figureColour){
        return userLookup.getSecond(figureColour);
    }

    public void deathHandler(){

    }

    public void movePlayer(String username, Point teleportPosition){
        Player playerToMove= colourToPlayer(userLookup.getFirst(username));
        playerToMove.teleport(teleportPosition);
    }
}
