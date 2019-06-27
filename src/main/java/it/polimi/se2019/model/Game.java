package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.utility.*;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.view.MVEvent;

import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class Game extends Observable<MVEvent> {
    private GameMap gameMap;
    private boolean finalFrenzy= true;
    private KillshotTrack killshotTrack;

    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck lootDeck;

    private List<Player> players= new ArrayList<>();
    private BiSet<FigureColour, String> userLookup = new BiSet<>();
    private List<String> usernames= new ArrayList<>();

    private TurnMemory turnMemory = new TurnMemory();

    private WeaponHelper weaponHelper=new WeaponHelper();
    private ComboHelper comboHelper=new ComboHelper();
    private PowerUpHelper powerUpHelper=new PowerUpHelper();
    private LootCardHelper lootCardHelper=new LootCardHelper();

    private List <Integer> pointsToAssign= new ArrayList<>(Arrays.asList(8, 6, 4, 2, 1, 1, 1, 1));
    private List <Integer> frenzyPointsToAssign= new ArrayList<>(Arrays.asList(2, 1, 1, 1, 1));

    private MVSelectionEvent selectionEventHolder = null;

    public Game(){
        weaponDeck= new Deck(new ArrayList<>(weaponHelper.getWeapons()));
        powerUpDeck= new Deck(new ArrayList<>(powerUpHelper.getPowerUps()));
        lootDeck= new Deck(new ArrayList<>(lootCardHelper.getLootCards()));
        observers = new ArrayList<>();
    }

    public void send (MVEvent event){
        notify(event);
    }

    public void apply (String playing, List<Player> players, PartialWeaponEffect partialWeaponEffect){
       for (Player p : players)
           userToPlayer(playing).apply(p, partialWeaponEffect);
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

    public void pausePlayer (String username){
        Player player= userToPlayer(username);
        player.pause();
    }

    public void unpausePlayer (String username){
        Player player= userToPlayer(username);
        player.unpause();
    }

    public void setTurnMemory(TurnMemory turnMemory) {
        this.turnMemory = turnMemory;
    }

    public Tile getTile (Point position){
        return gameMap.getTile(position);
    }

    public List<Integer> getFrenzyPointsToAssign() {
        return frenzyPointsToAssign;
    }

    public List<Integer> getPointsToAssign() {
        return pointsToAssign;
    }

    public boolean isFinalFrenzy() {
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

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public List<Player> getPlayers() { return players; }

    public List<String> getUsernames() {
        return usernames;
    }

    public BiSet<FigureColour, String> getUserLookup() {
        return userLookup;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setUserLookup(BiSet<FigureColour, String> userLookup) {
        this.userLookup = userLookup;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public void setKillshotTrack(KillshotTrack killshotTrack) {
        this.killshotTrack = new KillshotTrack(killshotTrack.getNumberOfSkulls());
    }

    public void setFinalFrenzy(boolean finalFrenzy) {
        this.finalFrenzy = finalFrenzy;
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
        return (Weapon)weaponHelper.findByName(weaponName);
    }

    public PowerUp nameToPowerUp (String powerUpName){
        return (PowerUp)powerUpHelper.findByName(powerUpName);
    }

    public LootCard nameToLootCard (String lootCardName){
        return (LootCard)lootCardHelper.findByName(lootCardName);
    }

    public List<Tear> getHp (String username){
        return userToPlayer(username).getHp();
    }

    public void deathHandler (Player deadPlayer){
        notify(new MVDeathEvent("*",
                colourToUser(deadPlayer.getFigure().getColour()),
                colourToUser(deadPlayer.getHp().get(10).getColour()),
                (deadPlayer.getHp().size()==12),
                killshotTrack.getNumberOfSkulls()==killshotTrack.getKillshot().size()));
        //TODO: Changed for it to work in the tests
        killshotTrack.addKillshot(deadPlayer.getHp().get(10).getColour(), deadPlayer.getHp().size()==12);
        //if number of skulls equals dimension of killshot track, match is over
        //calculate points of all players, move all lines beneath this in DeathController
        //in DeathController for (Player p: players) calculatePoints and then verify if FF
        if (killshotTrack.getKillshot().size()==killshotTrack.getNumberOfSkulls()){
            if (finalFrenzy){
                frenzyUpdatePlayerStatus(deadPlayer);
                notify(new FinalFrenzyStartingEvent("*"));
            }
        }
    }

     public void updateKillshotTrack(FigureColour killer, boolean overkill){
        killshotTrack.addKillshot(killer, overkill);
         if (killshotTrack.getKillshot().size()==killshotTrack.getNumberOfSkulls())
             notify(new FinalFrenzyStartingEvent("*"));
     }

     public TurnMemory getTurnMemory (){
        return new TurnMemory(turnMemory);
     }

    public void allowedMovements (String username, String target, int radius){
        Player playing;
        if(target.equals(""))
            playing = userToPlayer(username);
        else
            playing= userToPlayer(target);
        List<Point> allowedPositions= gameMap.getAllowedMovements(playing.getFigure().getTile(), radius);
        if (!allowedPositions.isEmpty())
            notify(new AllowedMovementsEvent(username, allowedPositions, target));
        else
            throw new NullPointerException("List of possible movements is empty");
    }

    //TODO: understand where and how this method should be used
    public void sendPossibleTargets (String username, List<Player> players, List<Tile> tiles, boolean isArea) {
        List<String> usernames = new ArrayList<>();
        List<Point> points = new ArrayList<>();

        for (Player p : players)
            usernames.add(playerToUser(p));
    }

    //TODO: same as above
    public GraphNode<GraphWeaponEffect> getWeaponEffects (String weapon){
        return nameToWeapon(weapon).getDefinition();
    }

    // all players without any damage change their boards to final frenzy boards
    // final frenzy players get a different set of moves based on their position in the current
    public void frenzyUpdatePlayerStatus (Player deadPlayer){
        List<Player> beforeFirst = new ArrayList<>();
        List<Player> afterFirst = new ArrayList<>();
        boolean isBeforeFirst = false;

        for (Player p: players){
            if (p.equals(deadPlayer))
                isBeforeFirst = true;
            if (!isBeforeFirst && p.getHp().size()==0)
                afterFirst.add(p);
            else if (isBeforeFirst && p.getHp().size()==0)
                beforeFirst.add(p);
        }

        for (Player p: beforeFirst)
            p.updatePlayerDamage(new FinalFrenzyBeforeFirst());

        for (Player p: afterFirst)
            p.updatePlayerDamage(new FinalFrenzyStandard());
    }

    public List<String> getMapConfigs(){
        List<String> names=new ArrayList<>();
        Pattern pattern=Pattern.compile(".json");
        for (String name: Paths.get("files/mapConfigs").toFile().list()){
            names.add(pattern.matcher(name).replaceAll(""));
        }
        return names;
    }

    public void unloadedWeapons (String username){
        Player player= userToPlayer(username);
        List<String> unloadedWeapons= new ArrayList<>();
        for (Weapon w: player.getWeapons())
            unloadedWeapons.add(w.getName());
        send(new ReloadableWeaponsEvent(username, unloadedWeapons));
    }

    public ComboHelper getComboHelper() {
        return new ComboHelper(comboHelper);
    }

    public LootCardHelper getLootCardHelper() {
        return new LootCardHelper(lootCardHelper);
    }

    public PowerUpHelper getPowerUpHelper() {
        return new PowerUpHelper(powerUpHelper);
    }

    public WeaponHelper getWeaponHelper(){
        return new WeaponHelper(weaponHelper);
    }

    public void hit (String partialWeaponEffect, List<Targetable> hitTargets, Targetable target){
        target.hit(partialWeaponEffect, hitTargets, turnMemory);
    }

    public void usablePowerUps (String powerUpType, boolean costs, Player currentPlayer) {  //if player's turn, player owns a power up of this type
        if (!costs || !currentPlayer.getAmmo().isEmpty()) {
            for (PowerUp p : currentPlayer.getPowerUps()) {
                if (p.getConstraint().equals(powerUpType))
                    send(new UsablePowerUpEvent(playerToUser(currentPlayer), p.getName(), costs));
            }
        }
    }
}
