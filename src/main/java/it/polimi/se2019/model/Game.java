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

    public void timerTick(int timeToGo){

        notify(new TimerEvent("*", timeToGo));
    }

    public void newPlayerInMatchMaking(String token, String username){
        notify(new MvJoinEvent(token, username));
    }

    public void playerReconnection(String token, String oldToken, boolean isMatchMaking){
        notify(new MvReconnectionEvent(token, oldToken, isMatchMaking));
    }

    public void usernameDeletion(String username){
        notify(new UsernameDeletionEvent("*", username));
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

    public static void setInstance(Game instance) {
        Game.instance = instance;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

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

    public Player userToPlayer (String username){
        return colourToPlayer(userLookup.getFirst(username));
    }

    public Weapon nameToWeapon (String weaponName){
        return null;
    } //TODO: implement, Weapon should contain Map <String, Weapon>

    public PowerUp nameToPowerUp (String powerUpName){
        return null;
    } //TODO: implement, PowerUp should contain Map <String, Weapon>

    public void deathHandler(Player player){
        player.calculatePoints(player);
        player.updatePointsToAssign();
        updateKillshotTrack(player);
        player.setHp(null);
        //MVEvent: you're dead, draw a card and respawn
    }

     private void updateKillshotTrack(Player deadPlayer){
        FigureColour killer= deadPlayer.getHp().get(10).getColour(); //the 11th shot causes the death of the figure
         Boolean overkill= false;
         if (deadPlayer.getHp().size()==12){
             overkill= true; //if a 12th element is present in the list, overkill
             colourToPlayer(killer).addMark(deadPlayer.getFigure().getColour()); //overkiller receives a mark from deadplayer
         }
        killshotTrack.addKillshot(killer, overkill);
         if (killshotTrack.getKillshot().size()==killshotTrack.getNumberOfSkulls()){
             finalFrenzy();
         }
     }

     public void updateTurns(Player player, TurnMemory turnMemory){
        turns.set(players.indexOf(player), turnMemory.getTurn());
     }

     public void finalFrenzy (){

     }

    //exposed methods, used by controller

    public void teleportPlayer (String username, Point teleportPosition){
        Player playerToMove= userToPlayer(username);
        playerToMove.teleport(teleportPosition);
    }

    public void reloadWeapon (String username, String weaponName){
        Player playerReloading= userToPlayer(username);
        if (weaponName.equals(playerReloading.getFirstWeapon().getName())){
            playerReloading.reload(playerReloading.getFirstWeapon());
        }
        else if (weaponName.equals(playerReloading.getSecondWeapon().getName())){
            playerReloading.reload(playerReloading.getSecondWeapon());
        }
        else if (weaponName.equals(playerReloading.getThirdWeapon().getName())){
            playerReloading.reload(playerReloading.getThirdWeapon());
        }
    }

    public void run (String username, Point destination){
        Player playerRunning= userToPlayer(username);
        playerRunning.run(destination);
    }

    public void grab (String username){
        Player playerGrabbing= userToPlayer(username);
        playerGrabbing.grabStuff();
    }

    public void shoot (String username, String weapon, ArrayList<String> effects, ArrayList<ArrayList<String>> targetNames){
        Player shooter= userToPlayer(username);
        for (ArrayList<String> targetCounter: targetNames){
            int actionIndex= targetNames.indexOf(targetCounter);
            for (String counter: targetCounter){
                Player target= userToPlayer(counter);
                shooter.useWeapon(nameToWeapon(weapon), target, effects.get(actionIndex));
            }
        }
    }

    public void spawn (String username, AmmoColour spawnColour){
        Player spawning= userToPlayer(username);
        for (Tile tile: GameMap.getTiles()){
            if (tile.getColour().equals(spawnColour)&&tile.getTileType().equals(TileType.SPAWNTILE)){
                spawning.run(tile.position);
            }
        }
    }

    public void usePowerUp (String username, String powerUpName){
        Player player= userToPlayer(username);
        PowerUp powerUp= nameToPowerUp(powerUpName);
        player.usePowerUp(powerUp);
    }

}
