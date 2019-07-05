package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.mv_events.*;
import it.polimi.se2019.commons.utility.BiSet;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.commons.utility.Observable;
import it.polimi.se2019.client.view.MVEvent;

import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This class communicates with the virtual view. It holds all of the main data of the current game as the chosen map configuration,
 * the number of skulls of the match, whether final frenzy is activated, the killshot track on which all the deaths are stored,
 * The current players, their colours and usernames and the turn memory.
 */

public class Game extends Observable<MVEvent> {
    private GameMap gameMap;
    private boolean finalFrenzy= true;
    private KillshotTrack killshotTrack;
    private List<Point> emptyLootTiles = new ArrayList<>();
    private List<Point> emptySpawnTiles = new ArrayList<>();

    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck lootDeck;

    private List<Player> players= new ArrayList<>();
    private BiSet<FigureColour, String> userLookup = new BiSet<>();
    private List<String> usernames= new ArrayList<>();
    private List<String> playersWaitingToRespawn = new ArrayList<>();

    private TurnMemory turnMemory = new TurnMemory();

    private WeaponHelper weaponHelper=new WeaponHelper();
    private ComboHelper comboHelper=new ComboHelper();
    private PowerUpHelper powerUpHelper=new PowerUpHelper();
    private LootCardHelper lootCardHelper=new LootCardHelper();

    private List <Integer> pointsToAssign= new ArrayList<>(Arrays.asList(8, 6, 4, 2, 1, 1, 1, 1));
    private List <Integer> frenzyPointsToAssign= new ArrayList<>(Arrays.asList(2, 1, 1, 1, 1));

    public Game(){
        weaponDeck= new Deck(new ArrayList<>(weaponHelper.getWeapons()));
        powerUpDeck= new Deck(new ArrayList<>(powerUpHelper.getPowerUps()));
        lootDeck= new Deck(new ArrayList<>(lootCardHelper.getLootCards()));
        observers = new ArrayList<>();
    }

    /**
     * notifies the virtual view
     * @param event
     */
    public void send (MVEvent event){
        notify(event);
    }

    public void apply (String playing, List<Player> players, PartialWeaponEffect partialWeaponEffect){
       for (Player p : players)
           userToPlayer(playing).apply(p, partialWeaponEffect);
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

    public List<String> getPlayersWaitingToRespawn() {
        return new ArrayList<>(playersWaitingToRespawn);
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

    /**
     * handles the death of a player by notifying all the other users and adding them to a list of players waiting to respawn.
     * @param deadPlayer the player that died.
     */
    public void deathHandler (Player deadPlayer){
        updateKillshotTrack(deadPlayer.getHp().get(10).getColour(), deadPlayer.getHp().size()==12);
        notify(new MVDeathEvent("*",
                colourToUser(deadPlayer.getFigure().getColour()),
                colourToUser(deadPlayer.getHp().get(10).getColour()),
                (deadPlayer.getHp().size()==12),
                killshotTrack.getNumberOfSkulls()==killshotTrack.getKillshot().size()
                ));
        playersWaitingToRespawn.add(playerToUser(deadPlayer));

    }

    /**
     * updates the killshot track whenever a user dies with the data about their killer and the overkill.
     * @param killer the user that caused the death.
     * @param overkill whether the killer overkilled.
     */
     public void updateKillshotTrack (FigureColour killer, boolean overkill) {
         killshotTrack.addKillshot(killer, overkill);
         if (killshotTrack.getKillshot().size() == killshotTrack.getNumberOfSkulls()) {
             if (finalFrenzy) {
                 frenzyUpdatePlayerStatus(colourToPlayer(killer));
                 notify(new FinalFrenzyStartingEvent("*"));
             }
         }
     }

     public TurnMemory getTurnMemory (){
        return turnMemory;
     }

    /**
     * calculates the movements a player is allowed to perform.
     * @param username the user deciding the moves.
     * @param target the target moved, can be the first username as well.
     * @param radius the maximum distance.
     */
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

    /**
     * All players without any damage change their boards to final frenzy boards.
     * final frenzy players get a different set of moves based on their position in the current.
     * @param killer
     */
    public void frenzyUpdatePlayerStatus (Player killer){
        List<Player> beforeFirst = new ArrayList<>();
        List<Player> afterFirst = new ArrayList<>();

        for (Player p: players){
            if (players.indexOf(p) >= players.indexOf(killer))
                afterFirst.add(p);
            else if (players.indexOf(p)< players.indexOf(killer))
                beforeFirst.add(p);

        }

        for (Player p: beforeFirst) {
            p.updatePlayerDamage(new FinalFrenzyBeforeFirst());
            p.setPlayerValue(new ThirdDeath());
        }

        for (Player p: afterFirst) {
            p.updatePlayerDamage(new FinalFrenzyStandard());
            p.setPlayerValue(new ThirdDeath());
        }

    }

    /**
     * returns the map configurations generated through the JSON files.
     * @return the list of names of the available map configurations.
     */
    public List<String> getMapConfigs(){
        List<String> names=new ArrayList<>();
        Pattern pattern=Pattern.compile(".json");
        for (String name: Paths.get("files/mapConfigs").toFile().list()){
            names.add(pattern.matcher(name).replaceAll(""));
        }
        return names;
    }

    /**
     * returns the weapons owned that are not loaded in order to reload them.
     * @param username the user whose weapons are to be reloaded.
     */
    public void unloadedWeapons (String username){
        HashMap<String, ArrayList<String>> unloadedWeapons= new HashMap<>();
        ArrayList<String> ammos = new ArrayList<>();
        for (Weapon w: userToPlayer(username).getWeapons()) {
            for (Ammo a : w.getPrice())
                ammos.add(a.getColour().name());
            ammos.add(w.cardColour.getColour().name());
            unloadedWeapons.put(w.getName(), ammos);
            ammos.clear();
        }
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

    /**
     * Calculates which power ups can be used given their constraint.
     * @param powerUpType the constraint that is being considered (eg: during the player's turn, while attacking, while beinng attacked).
     * @param costs whether the usage would cost.
     * @param currentPlayer the player whose power ups are being evaluated.
     */
    public void usablePowerUps (String powerUpType, boolean costs, Player currentPlayer) {  //if player's turn, player owns a power up of this type
        if (!costs || !currentPlayer.getAmmo().isEmpty()) {
            for (PowerUp p : currentPlayer.getPowerUps()) {
                if (p.getConstraint().equalsIgnoreCase(powerUpType))
                    send(new UsablePowerUpEvent(playerToUser(currentPlayer), p.getName(), costs));
            }
        }
    }

    public void removeFromWaitingList (String player){
        playersWaitingToRespawn.remove(player);
    }

    public void addEmptyLootTile (Point point){
        emptyLootTiles.add(point);
    }

    public void addEmptySpawnTile (Point point){
        emptySpawnTiles.add(point);
    }

    public void emptyEmptySpawnTile(){emptySpawnTiles.clear();}

    public void emptyEmptyLootTile(){emptyLootTiles.clear();}

    public List<Point> getEmptyLootTiles() {
        return new ArrayList<>(emptyLootTiles);
    }

    public List<Point> getEmptySpawnTiles() {
        return new ArrayList<>(emptySpawnTiles);
    }
}
