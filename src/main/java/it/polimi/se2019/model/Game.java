package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.utility.BiSet;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Pair;
import it.polimi.se2019.view.MVEvent;

import java.util.*;

public class Game extends Observable<MVEvent> {
    private GameMap gameMap;
    private Boolean finalFrenzy;
    private KillshotTrack killshotTrack;
    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck lootDeck;
    private static Game instance=null;
    private List<Player> players= new ArrayList<>();
    private List<Turn> turns;
    private BiSet<FigureColour, String> userLookup = new BiSet<>();
    private Map<String, Effect> effectMap; //getEffect in Card class
    private Random randomConfig= new Random();

    // TODO Mapping between figures and usernames coming from controller

    private Game(){
        weaponDeck= new WeaponDeck();
        powerUpDeck= new PowerUpDeck();
        //lootDeck= new LootDeck();
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
        int chosenConfig=-1;
        int numberOfLootCards=7;
        HashMap<String, FigureColour> userToColour= new HashMap<>();
        List<String> weaponSpots= new ArrayList<>();
        List<String> lootCards= new ArrayList<>();
        for (String userCounter: usernames){
            userLookup.add(new Pair<>(FigureColour.values()[colourCounter], userCounter));
            players.add(new Player(new Figure(FigureColour.values()[colourCounter], new Point(-1, -1))));
            colourCounter++;
        }
        if (((Integer) usernames.size()).equals(3)){
            chosenConfig= randomConfig.nextInt(2);
        }
        else if (((Integer) usernames.size()).equals(4)){
            chosenConfig= randomConfig.nextInt(3);
        }
        else if (((Integer) usernames.size()).equals(5)){
            chosenConfig= randomConfig.nextInt(2)+1;
        }
        //TODO: initialize gameMap
        for (Player player: players){
            userToColour.put(userLookup.getSecond(player.getFigure().getColour()), player.getFigure().getColour());
        }
        for (int i=0; i<9; i++) {
            weaponSpots.add(weaponDeck.draw().getName());
        }
        numberOfLootCards+=chosenConfig;
        for (int i=0; i<numberOfLootCards; i++){
            lootCards.add(lootDeck.draw().getName());
        }
        killshotTrack= new KillshotTrack(randomConfig.nextInt(4)+5);

        notify(new MatchMakingEndEvent("*", chosenConfig, userToColour, weaponSpots, lootCards, killshotTrack.getNumberOfSkulls()));
    }

    public void startMatch(){
        Card firstCard= powerUpDeck.draw();
        Card secondCard= powerUpDeck.draw();
        notify(new StartFirstTurnEvent(colourToUser(players.get(0).getFigure().getColour()),
                firstCard.getCardColour().toString(),
                firstCard.getName(),
                secondCard.getCardColour().toString(),
                secondCard.getName()
                ));
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
        if (instance==null){
            instance= new Game();
        }
        return instance;
    }

    public Boolean getFinalFrenzy() {
        return finalFrenzy;
    }

    public Deck getLootDeck() { return lootDeck; }

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

    public void setLootDeck(Deck lootDeck) {
        this.lootDeck = lootDeck;
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
        for (Player playerCounter: players){
            if (playerCounter.getFigure().getColour().equals(figureColour)){
                return playerCounter;
            }
        }
        return null;
    }

    public String colourToUser (FigureColour figureColour){
        return userLookup.getSecond(figureColour);
    }

    public Player userToPlayer (String username){
        return colourToPlayer(userLookup.getFirst(username));
    }

    public Weapon nameToWeapon (String weaponName){
        return CardHelper.getInstance().findWeaponByName(weaponName);
    }

    public PowerUp nameToPowerUp (String powerUpName, AmmoColour powerUpColour){
        return CardHelper.getInstance().findPowerUpByName(powerUpName, powerUpColour);
    }

    public void deathHandler(Player player){
        player.calculatePoints(player);
        updateKillshotTrack(player);
        player.setHp(null);
        notify(new DeathEvent(colourToUser(player.getFigure().getColour()), player.getHp().get(10).getColour().toString()));
        if (killshotTrack.getNumberOfSkulls().equals(killshotTrack.getKillshot().size())){
            //finalFrenzyTurn: change status of players, change moves
        }
    }

    public void finalFrenzyDeathHandler (Player player){
        player.finalFrenzyCalculatePoints(player);
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
             notify(new FinalFrenzyStartingEvent("*"));
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

    public void usePowerUp (String username, String powerUpName, AmmoColour powerUpColour){
        Player player= userToPlayer(username);
        PowerUp powerUp= nameToPowerUp(powerUpName, powerUpColour);
        player.usePowerUp(powerUp);
    }
}
