package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.utility.*;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.view.MVEvent;

import java.util.*;

public class Game extends Observable<MVEvent> {
    private GameMap gameMap;
    private boolean finalFrenzy= true;
    private KillshotTrack killshotTrack;
    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck lootDeck;
    private List<Player> players= new ArrayList<>();
    private BiSet<FigureColour, String> userLookup = new BiSet<>();
    private Random randomConfig= new Random();
    private TurnMemory turnMemory = new TurnMemory();

    private MVSelectionEvent selectionEventHolder = null;

    // TODO Mapping between figures and usernames coming from controller

    public Game(){
        weaponDeck= new Deck(new ArrayList<>(CardHelper.getInstance().getAllWeapons()));
        powerUpDeck= new Deck(new ArrayList<>(CardHelper.getInstance().getAllPowerUp()));
        lootDeck= new Deck(new ArrayList<>(CardHelper.getInstance().getAllLootCards()));
        observers = new ArrayList<>();
    }

    public void sendPossibleEffects (String username, String weaponName, List<GraphWeaponEffect> weaponEffect){
        PossibleEffectsEvent event = new PossibleEffectsEvent(username, weaponName);
        for (GraphWeaponEffect w: weaponEffect){
            event.addEffect(w.getName(), w.getEffectType());
        }
        notify(event);
    }

    public void addToSelection(String playerSelecting, List<Action> actions, List<Targetable> targetables){
        if(selectionEventHolder == (null))
            selectionEventHolder = new MVSelectionEvent(playerSelecting);
        if(!targetables.isEmpty())
            targetables.get(0).addToSelectionEvent(selectionEventHolder, targetables, actions);

    }

    public void sendPossibleTargets (){
        notify(selectionEventHolder);
        selectionEventHolder = null;
    }

    public void timerTick(int timeToGo){

        notify(new TimerEvent("*", timeToGo));
    }

    public void sendPartialEffectConflict (String username, List<ArrayList<Action>> possibleActions, List<String> previousTargets){
        notify(new PartialEffectEvent(username, possibleActions, previousTargets));
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
            players.add(new Player(new Figure(FigureColour.values()[colourCounter]), this));

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
            weaponSpots.add(((Weapon)weaponDeck.draw()).getName());
        }
        numberOfLootCards+=chosenConfig;
        for (int i=0; i<numberOfLootCards; i++){
            lootCards.add(((LootCard)lootDeck.draw()).getName());
        }
        killshotTrack= new KillshotTrack(randomConfig.nextInt(4)+5);

        notify(new MatchMakingEndEvent("*", chosenConfig, userToColour, weaponSpots, lootCards, killshotTrack.getNumberOfSkulls()));
    }

    public void startMatch(){
        PowerUp firstCard= (PowerUp) powerUpDeck.draw();
        PowerUp secondCard= (PowerUp) powerUpDeck.draw();
        notify(new StartFirstTurnEvent(colourToUser(players.get(0).getFigure().getColour()),
                firstCard.getCardColour().toString(),
                firstCard.getName(),
                secondCard.getCardColour().toString(),
                secondCard.getName()
                ));
    }

    public void startTurn (String playing){
        notify(new StartTurnEvent(playing));
    }

    public void endTurn (String username){
        Player player= userToPlayer(username);
    }

    public void pausePlayer (String username){
        Player player= userToPlayer(username);
        player.pause();
    }

    public void pausedPlayer (Player pausedPlayer){
        notify(new PausedPlayerEvent("*", colourToUser(pausedPlayer.getFigure().getColour())));
    }

    public void unpausePlayer (String username){
        Player player= userToPlayer(username);
        player.unpause();
    }

    public void unpausedPlayer (Player unpausedPlayer){
        notify(new UnpausedPlayerEvent("*", colourToUser(unpausedPlayer.getFigure().getColour())));
    }

    public Tile getTile (Point position){
        return gameMap.getMap().get(position);
    }

    public boolean getFinalFrenzy() {
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

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setUserLookup(BiSet<FigureColour, String> userLookup) {
        this.userLookup = userLookup;
    }

    public Player colourToPlayer (FigureColour figureColour){
        for (Player playerCounter: players){
            if (figureColour.equals(playerCounter.getFigure().getColour())){
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

    public String playerToUser (Player player){
        return colourToUser(player.getFigure().getColour());
    }

    public Weapon nameToWeapon (String weaponName){
        return CardHelper.getInstance().findWeaponByName(weaponName);
    }

    public PowerUp nameToPowerUp (String powerUpName){
        return CardHelper.getInstance().findPowerUpByName(powerUpName);
    }

    public LootCard nameToLootCard (String lootCardName){
        return CardHelper.getInstance().findLootCardByName(lootCardName);
    }

    public List<Tear> getHp (String username){
        return userToPlayer(username).getHp();
    }

    public void deathHandler (Player deadPlayer){
        notify(new MVDeathEvent("*",
                colourToUser(deadPlayer.getFigure().getColour()),
                colourToUser(deadPlayer.getHp().get(10).getColour())));
    }

     public void updateKillshotTrack(FigureColour killer, boolean overkill){
        killshotTrack.addKillshot(killer, overkill);
         if (killshotTrack.getKillshot().size()==killshotTrack.getNumberOfSkulls())
             notify(new FinalFrenzyStartingEvent("*"));
     }

     public TurnMemory getTurnMemory (){
        return new TurnMemory(turnMemory);
     }

     public List<Integer> getPointsToAssign (String username){
        Player player = userToPlayer(username);
        int index = player.getPointsToAssign().indexOf(player.getPlayerValue().getMaxValue());
        return new ArrayList<>(player.getPointsToAssign().subList(index, player.getPointsToAssign().size()));
     }
    //exposed methods, used for MVEvents or VCEvents

    public void allowedMovements (String username, int radius){
        Player playing= userToPlayer(username);
        List<Point> allowedPositions= new ArrayList<>();
        //TODO: calculates where the player can move
        notify(new AllowedMovementsEvent(username, allowedPositions));
    }

    public void allowedWeapons(String username){
        Player playing= userToPlayer(username);
        List<String> weapons= new ArrayList<>();
        for (Weapon weapon: playing.showWeapons()){
            weapons.add(weapon.getName());
        }
        notify(new AllowedWeaponsEvent(username, weapons));
    }
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

    public void playerMovement (Player playerRunning, Point finalPosition){
        notify(new MVMoveEvent("*", colourToUser(playerRunning.getFigure().getColour()), finalPosition));
    }

    public void grab (String username, String grabbed){
        Player playerGrabbing= userToPlayer(username);
        Grabbable grabbedCard= null;
        for (Weapon weapon: CardHelper.getInstance().getAllWeapons()){
            if (weapon.getName().equalsIgnoreCase(grabbed)){
                grabbedCard= nameToWeapon(grabbed);
                break;
            }
        }
        if (grabbedCard==null){
            for (LootCard lootCard: CardHelper.getInstance().getAllLootCards()){
                if (lootCard.getName().equalsIgnoreCase(grabbed)){
                    grabbedCard= nameToLootCard(grabbed);
                }
            }
        }
        if (grabbedCard==null) throw new NullPointerException("This card is not grabbable");
        playerGrabbing.grabStuff(grabbedCard);
    }

    public void sendAvailableWeapons (String username){
        Player shooting= userToPlayer(username);
        List<String> availableWeapons= new ArrayList<>();
        for (Weapon weapon: shooting.showWeapons()){
            availableWeapons.add(weapon.getName());
        }
        notify(new AvailableWeaponsEvent(username, availableWeapons));
    }

    public void sendPossibleEffects (String username, String weaponName){
        Weapon weapon= nameToWeapon(weaponName);

        /*notify(new PossibleEffectsEvent(username,
                weaponName,
                weapon.getCardColour().getColour().toString(),
                ));*/
    }

    public void sendPossibleTargets (String username, List<Player> players, List<Tile> tiles, boolean isArea) {
        List<String> usernames = new ArrayList<>();
        List<Point> points = new ArrayList<>();

        for (Player p : players) {
            usernames.add(playerToUser(p));
        }
    }


    public GraphNode<GraphWeaponEffect> getWeaponEffects (String weapon){
        return nameToWeapon(weapon).getDefinition();
    }

    public void spawn (String username, AmmoColour spawnColour, String powerUpName){
        Player spawning= userToPlayer(username);
        PowerUp drawnPowerUp= nameToPowerUp(powerUpName);
        for (Tile tile: gameMap.getTiles()){
            if (tile.getColour().toString().equals(spawnColour.toString())&&tile.getTileType().equals(TileType.SPAWNTILE)){
                spawning.run(tile.position);
            }
        }
        if (drawnPowerUp!=null){
            spawning.drawPowerUp(drawnPowerUp);
        }
        notify(new MVMoveEvent("*", username, spawning.getFigure().getPosition()));
        notify(new StartTurnEvent(username));
    }

    public void usePowerUp (String username, String powerUpName){
        Player player= userToPlayer(username);
        PowerUp powerUp= nameToPowerUp(powerUpName);
        player.usePowerUp(powerUp);
    }

    public void chosePowerUpToDiscard (Player player, List<PowerUp> powerUps){
        String username= colourToUser(player.getFigure().getColour());
        List<String> powerUpsToDiscard= new ArrayList<>();
        for (PowerUp powerUp: powerUps){
            powerUpsToDiscard.add(powerUp.getName());
        }
        notify(new PowerUpToLeaveEvent(username, powerUpsToDiscard));
    }

    public void discardPowerUp (String username, String powerUpName){
        Player playing= userToPlayer(username);
        PowerUp powerUpToDiscard= nameToPowerUp(powerUpName);
        //playing.discardPowerUp(powerUpToDiscard); //TODO solve called method's todo
    }

    public void discardedPowerUp (Player player, PowerUp drawnPowerUp, PowerUp discardedPowerUp){
        String username= colourToUser(player.getFigure().getColour());
        notify(new DiscardedPowerUpEvent("*", username, drawnPowerUp.getName(), discardedPowerUp.getName()));
    }

    public void attackOnPlayer (Player attacked, Player attacker){
        notify(new UpdateHpEvent("*", colourToUser(attacked.getFigure().getColour()), colourToUser(attacker.getFigure().getColour())));
    }

    public void markOnPlayer (Player marked, Player marker){
        notify(new UpdateMarkEvent("*", colourToUser(marked.getFigure().getColour()), colourToUser(marker.getFigure().getColour())));
    }

    public void updatePoints (String username, int points){
        userToPlayer(username).setPoints(userToPlayer(username).getPoints()+points);
        notify(new UpdatePointsEvent(username, userToPlayer(username).getPoints()));
    }

}
