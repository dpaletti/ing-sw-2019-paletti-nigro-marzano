package it.polimi.se2019.model;

import it.polimi.se2019.model.MVEvents.MatchMakingEndEvent;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.model.MVEvents.UsernameDeletionEvent;
import it.polimi.se2019.model.MVEvents.UsernameEvaluationEvent;

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
    private Map<FigureColour, String> userLookup;
    // TODO Mapping between figures and usernames coming from controller


    public void invalidUsername(String bootstrapId, List<String> usernames){
        notify(new UsernameEvaluationEvent(bootstrapId, usernames));
    }

    public void validUsername(String username, String password){
        notify(new UsernameEvaluationEvent(username, password));
    }

    public void usernameDeletion(String username){
        notify(new UsernameDeletionEvent(username));
    }

    public void closeMatchMaking(List<String> usernames){
        // '*' is a wildcard, it means that the event goes to everybody
        notify(new MatchMakingEndEvent("*"));
    }

    public void startMatch(){
        //TODO implement this
    }

    public void pausePlayer(String username){
        //When a player disconnects or times out while playing needs to be paused
        //upone reconnection it will be unpaused
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

    public void deathHandler(){

    }
}
