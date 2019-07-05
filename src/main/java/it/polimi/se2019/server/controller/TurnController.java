package it.polimi.se2019.server.controller;

import it.polimi.se2019.server.model.*;
import it.polimi.se2019.commons.mv_events.*;
import it.polimi.se2019.server.network.Server;
import it.polimi.se2019.commons.utility.JsonHandler;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.server.model.PartialCombo;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.vc_events.*;
import it.polimi.se2019.commons.vc_events.DiscardedPowerUpEvent;

import java.util.*;

/**
 * This class handles the interaction with a user throughout its turn, showing them the possible combos to use,
 * allowing them to spawn or respawn and refreshing the game board at the end of the turn before moving on.
 * See {@link it.polimi.se2019.server.controller.Controller}.
 */

public class TurnController extends Controller {
    private boolean isFirstTurn = true;
    private String currentPlayer;
    private Combo currentCombo;

    private int comboIndex = 0;
    private int comboUsed = 0;
    private boolean reloaded = false;
    private boolean spawning = true;
    private boolean finalFrenzyTurn = false;
    private int frenzyTurnCounter = 0;

    private int turnCounter = 0;

    private TickingTimer interTurnTimer;
    private TickingTimer turnTimer;

    //should if(currentCombo.getPartialCombos().get(comboIndex).equals(PartialCombo.WHATEVER)) be checked for Move, grab and shoot as well as reload?

    public TurnController (Game model, Server server, int roomNumber){
        super(model, server, roomNumber);
        currentPlayer = model.getUsernames().get(0);
        turnTimer= new TickingTimer(model, this::interTurn);
        interTurnTimer= new TickingTimer(model, this::endTurn);
        //turn controller is registered to virtualView in closeMatchMaking() inside MatchMaking controller
        //either leave things like this or take that one out and add server.addController(this) here
    }

    //Test constructor
    public TurnController (Game game,Server server){
        this.model = game;
        currentPlayer = model.getUsernames().get(0);
        this.server=server;
        turnTimer= new TickingTimer(model, this:: interTurn);
        interTurnTimer= new TickingTimer(model, this::endTurn);
    }

    /**
     * This method ignores the events that are not dispatched in this controller.
     * @param message Any message arriving from the view.
     */
    @Override
    public void update(VCEvent message) {
        if(disabled)
            return;
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //ignore events that this controller does not support
            Log.fine("TurnController ignored " + JsonHandler.serialize(message));
        }
    }

    /**
     * Handles disconnection of a user while their turn is taking place.
     * @param message
     */
    @Override
    public void dispatch(VCFinalFrenzy message) {
        comboUsed=-1;
        comboIndex=0;
        currentCombo=null;
        nextCombo();
    }

    @Override
    public void dispatch(DisconnectionEvent message) {
        model.pausePlayer(message.getSource());
        if (message.getSource().equals(currentPlayer))
        //Turn controller only manages disconnection logic for middle-turn disconnections
            endTurn();
    }

    @Override
    public void dispatch(VcReconnectionEvent message) {
        Log.fine("Handling " + message);
        model.unpausePlayer(message.getUsername());
    }

    /**
     * Handles a player spawning or respawning.
     * @param message
     */

    @Override
    public void dispatch(SpawnEvent message) {
        boolean isRespawn = false;
        spawning = false;
        if (model.getPlayersWaitingToRespawn().contains(message.getSource())) {
            model.removeFromWaitingList(message.getSource());
            isRespawn = true;
        }
        spawn(message.getSource(), stringToAmmo(message.getDiscardedPowerUpColour()), message.getPowerUpToKeep(), isRespawn);
        if (isRespawn){
            if (model.getPlayersWaitingToRespawn().isEmpty())
                interTurnTimer.endTimer();
        }
    }

    /**
     * Reloads a weapon the user selects.
     * @param message
     */

    @Override
    public void dispatch(ReloadEvent message) {
        for (String s : message.getReloadedWeapons())
            reloadWeapon(message.getSource(), s);
        if (currentCombo == null)
            reloaded = true;
        else if (currentCombo.getPartialCombos().get(comboIndex).equals(PartialCombo.RELOAD))
            nextPartialCombo();
        if (comboUsed==2 || reloaded)
            turnTimer.endTimer();
        }

    /**
     *Verifies whether the combo is valid or not
     * @param combos a list of partial combos that make up a combo.
     * @param combo a combo.
     * @return true whether the combo is valid.
     */
        private boolean isCombo(List<PartialCombo> combos,Combo combo){
            List<PartialCombo> expected= combo.getPartialCombos();
            if(combos.size()!=expected.size())
                return false;
            for (int i=0;i<expected.size();i++){
                if (!expected.get(i).equals(combos.get(i)))
                    return false;
            }
            return true;
        }

    /**
     * Parses a list of partial combos and transforms them into a combo.
     * @param partialCombos the list of partial combos to transform.
     * @return the combo generated.
     */

    private Combo fromPartialToCombo(List<PartialCombo> partialCombos){
            PlayerDamage playerState=model.userToPlayer(currentPlayer).getHealthState();
            Set<Combo> allCombos= model.getComboHelper().getCombos();
            List<Combo> usables= new ArrayList<>();
            if(playerState.isFinalFrenzy()){
                for (Combo combo: allCombos){
                    if(combo.getName().contains("Frenzy"))
                        usables.add(combo);
                }
            }else{
                for (Combo combo:allCombos){
                    if(!combo.getName().contains("Frenzy"))
                        usables.add(combo);
                }
            }
            for(Combo c: usables){
                if(isCombo(partialCombos,c))
                    return c;
            }
            throw new IllegalArgumentException("No combo exist with such partialCombos: "+partialCombos);
        }

    /**
     * converts partials to list of combos.
     * @param partials a list of partials to convert.
     * @return a list of combos converted.
     */
    private List<Combo> convertMoves(List<ArrayList<PartialCombo>> partials){
            List<Combo> combos = new ArrayList<>();
            for(ArrayList<PartialCombo> p: partials){
                combos.add(fromPartialToCombo(p));
            }
            return combos;
        }

    /**
     * transforms combos to their strings.
     * @param combos the combos to be transformed.
     * @return the strings of those combos.
     */

    private List<String> movesToString(List<Combo> combos){
            List<String> strings = new ArrayList<>();
            for (Combo c: combos)
                strings.add(c.getName());
            return strings;
        }

    /**
     * transforms a list of partials to a list of combos.
     * @param partials a list of partials to be transformed.
     * @return the transformed strings.
     */
    private List<String> fromPartialToStringCombo(List<ArrayList<PartialCombo>> partials){
            return movesToString
                    (convertMoves
                            (partials));
        }

    /**
     * The chosen combo is dispatched and the allowed actions of that combo are notified to the user.
     * @param message
     */
    @Override
    public void dispatch(ChosenComboEvent message) {
        disablePowerUps(currentPlayer,"onAttack");
        comboIndex = 0;
        currentCombo = (Combo) model.getComboHelper().findByName(message.getChosenCombo());
        currentCombo.getPartialCombos().get(comboIndex).use(model, message.getSource());

    }

    /**
     * Whenever a user decides to end their turn before the turn is technically over, this method ends their turn.
     * @param message
     */
    @Override
    public void dispatch(VCEndOfTurnEvent message) {
        turnTimer.endTimer();
    }

    /**
     * Moves the user to the specified tile.
     * @param message
     */

    @Override
    public void dispatch(VCMoveEvent message) {
        int distance = 1;
        if (message.getIsTeleport())
            distance = -1;
        run(message.getToMove(), message.getDestination(), distance);
        if(currentCombo==null)
            return;
        if (currentCombo.getPartialCombos().get(comboIndex).equals(PartialCombo.SHOOT))
            return;
        nextPartialCombo();
    }

    /**
     * Grabs the chosen grabbable.
     * @param message
     */

    @Override
    public void dispatch(GrabEvent message) {
        model.userToPlayer(message.getSource()).grabStuff(message.getGrabbed());
        nextPartialCombo();
    }

    /**
     * moves to the following combo the user can perform.
     */

    private void nextCombo(){
        PlayerDamage playerDamage=model.userToPlayer(currentPlayer).getHealthState();
        if (playerDamage.isFinalFrenzy()) {
            if (!playerDamage.isBefore()) {
                comboUsed++;
                if (comboUsed == 1) {
                    model.unloadedWeapons(currentPlayer);
                    return;
                }else{
                    model.send(new TurnEvent(currentPlayer, fromPartialToStringCombo(getAllowedMoves())));
                    model.usablePowerUps("onTurn", false, model.userToPlayer(currentPlayer));
                    return;
                }
            }
        }
        comboUsed++;
        if (comboUsed == 2) {
            model.unloadedWeapons(currentPlayer);
            return;
        }
        model.send(new TurnEvent(currentPlayer, fromPartialToStringCombo(getAllowedMoves())));
        model.usablePowerUps("onTurn", false, model.userToPlayer(currentPlayer));
    }

    /**
     * moves to the following partial in the combo the user selected.
     */

    private void nextPartialCombo (){
        if(currentCombo == null)
            return;
        comboIndex++;
        if (comboIndex < currentCombo.getPartialCombos().size())
            currentCombo.getPartialCombos().get(comboIndex).use(model, currentPlayer);
        if (comboIndex == currentCombo.getPartialCombos().size()) {
            comboIndex = 0;
            nextCombo();
        }
    }

    @Override
    public void dispatch(DiscardedPowerUpEvent message) {
        model.userToPlayer(message.getSource()).discardPowerUp(message.getDiscardedPowerUp());
    }

    @Override
    public void dispatch(VCCardEndEvent message) {
        nextPartialCombo();
    }

    private void run (String username, Point destination, int distance){
        model.userToPlayer(username).run(destination, distance);
    }

    private void reloadWeapon (String username, String weaponName){
        model.userToPlayer(username).reload(model.nameToWeapon(weaponName));
    }

    /**
     * this method spawns a player to their preferred spawn tile and assigns them the power up that was not discarded.
     * @param username the user that is currently spawning.
     * @param spawnColour the colour of the tile the player wishes to spawn on.
     * @param powerUpName the name of the power up the user wants to keep.
     * @param respawn whether the player is respawning or not.
     */
    private void spawn (String username, AmmoColour spawnColour, String powerUpName, boolean respawn) {
        for (Tile tile : model.getGameMap().getSpawnTiles()) {
            if (tile.getColour().toString().equals(spawnColour.toString()))
                model.userToPlayer(username).getFigure().spawn(tile.getPosition());
        }
        if (model.nameToPowerUp(powerUpName) != null)
            model.userToPlayer(username).drawPowerUp(powerUpName);
        model.send(new MVMoveEvent("*", username, model.userToPlayer(username).getFigure().getPosition()));
        if (!respawn) {
            model.usablePowerUps("onTurn", false, model.userToPlayer(currentPlayer));
            model.send(new TurnEvent(username, fromPartialToStringCombo(model.userToPlayer(username).getHealthState().getMoves())));
        }
    }

    /**
     * This method ends the turn of a player and starts that of the following player in the match.
     */
    private void endTurn(){
        if(spawning) {
            spawn(currentPlayer,
                    model.userToPlayer(currentPlayer).getFirstPowerUp().getCardColour().getColour(),
                    model.userToPlayer(currentPlayer).getSecondPowerUp().getName(),
                    false);
            model.send(new MVDiscardPowerUpEvent("*", model.userToPlayer(currentPlayer).getFirstPowerUp().getName(), currentPlayer));
            model.userToPlayer(currentPlayer).setFirstPowerUp(null);
            model.userToPlayer(currentPlayer).setSecondPowerUp(null);
        }
        String previouslyPlaying = currentPlayer;
        comboUsed = 0;
        comboIndex = 0;
        currentCombo = null;
        turnCounter++;
        refreshBoard();
        if (enoughActivePlayers()){
            currentPlayer = getNextActiveUser(currentPlayer);
            model.send(new MVEndOfTurnEvent("*", previouslyPlaying, currentPlayer));
            disablePowerUps(previouslyPlaying,"onTurn");
            if (turnCounter == model.getUsernames().size())
                isFirstTurn = false;
            if (isFirstTurn) {
                model.userToPlayer(currentPlayer).setFirstPowerUp((PowerUp) model.getPowerUpDeck().draw());
                model.userToPlayer(currentPlayer).setSecondPowerUp((PowerUp) model.getPowerUpDeck().draw());
                model.send(new MVEndOfTurnEvent("*", previouslyPlaying, currentPlayer));
                disablePowerUps(previouslyPlaying,"onTurn");
                model.send(new StartFirstTurnEvent(currentPlayer,
                        model.userToPlayer(currentPlayer).getFirstPowerUp().getName(),
                                model.userToPlayer(currentPlayer).getSecondPowerUp().getName(),
                        false,
                        model.getGameMap().getMappedSpawnPoints()));
                model.usablePowerUps("onTurn", false, model.userToPlayer(currentPlayer));
                spawning = true;
            }
            else {
                model.send(new TurnEvent(currentPlayer,
                        fromPartialToStringCombo(getAllowedMoves())));
                model.usablePowerUps("onTurn", false, model.userToPlayer(currentPlayer));
            }
            turnTimer.startTimer(server.getTurnTimer());
        }
    }

    public int getComboIndex() {
        return comboIndex;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Calculates the combos that the user can actually perform.
     * @return a list of combos.
     */
    private List<ArrayList<PartialCombo>> getAllowedMoves(){
        List<ArrayList<PartialCombo>> allowed=new ArrayList<>();
        if(model.userToPlayer(currentPlayer).getLoadedWeapons().isEmpty()){
            for(ArrayList<PartialCombo> c: model.userToPlayer(currentPlayer).getHealthState().getMoves()){
                if(!c.contains(PartialCombo.SHOOT) || (c.contains(PartialCombo.SHOOT) && c.contains(PartialCombo.RELOAD))){
                    allowed.add(c);
                }
            }
        }else {
            allowed=model.userToPlayer(currentPlayer).getHealthState().getMoves();
        }
        return allowed;
    }

    /**
     * This method respawns users that died during the turn of the current player.
     */
    private void interTurn(){
        if (!model.getPlayersWaitingToRespawn().isEmpty()){
            for (String s : model.getPlayersWaitingToRespawn()) {
                model.userToPlayer(s).setFirstPowerUp((PowerUp)model.getPowerUpDeck().draw());
                model.userToPlayer(s).setSecondPowerUp(null);
                model.send(new MVRespawnEvent(s, model.userToPlayer(s).getFirstPowerUp().getName()));
                model.userToPlayer(s).getHp().clear();
            }
            interTurnTimer.startTimer(server.getInterTurnTimer());
        }else{
            endTurn();
        }
    }

    /**
     * Draws new cards and places them on the board, then sends the players a notification with the new cards placed on the board.
     */
    private void refreshBoard() {
        HashMap<Point, String> loot = new HashMap<>();
        HashMap<String, String> spawn = new HashMap<>();
        Drawable card;

        for (Point p : model.getEmptyLootTiles())
            model.getTile(p).add((LootCard)model.getLootDeck().draw());

        for (Point p : model.getEmptySpawnTiles()) {
            model.getTile(p).add((Weapon) model.getWeaponDeck().draw());
        }
        for (Tile t : model.getGameMap().getSpawnTiles()){
            for (int i = 0; i < t.getGrabbables().size(); i++)
                spawn.put(t.getGrabbables().get(i).getName(), t.getColour().name());
        }
        for(Tile t : model.getGameMap().getLootTiles()){
            loot.put(t.getPosition(), t.getGrabbables().get(0).getName());
        }
        model.send(new BoardRefreshEvent("*", spawn, loot));
        model.emptyEmptyLootTile();
        model.emptyEmptySpawnTile();
    }

    public void startTimer(){
        turnTimer.startTimer(server.getTurnTimer());
    }

    public void setFinalFrenzyTurn(boolean finalFrenzyTurn) {
        this.finalFrenzyTurn = finalFrenzyTurn;
    }

    public void setSpawning(boolean spawning) {
        this.spawning = spawning;
    }
}
