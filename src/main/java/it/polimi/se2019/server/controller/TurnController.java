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

    @Override
    public void dispatch(DisconnectionEvent message) {
        if(message.getSource().equals(currentPlayer))
            //Turn controller only manages disconnection logic for middle-turn disconnections

            dispatch(new VCEndOfTurnEvent(message.getSource()));
    }

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

        private Combo fromPartialToCombo(List<PartialCombo> partialCombos){
            PlayerDamage playerState=model.userToPlayer(currentPlayer).getHealthState();
            Set<Combo> allCombos= model.getComboHelper().getCombos();
            List<Combo> usables= new ArrayList<>();
            if(playerState.equals(new FinalFrenzyBeforeFirst()) || playerState.equals(new FinalFrenzyStandard())){
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

        private List<Combo> convertMoves(List<ArrayList<PartialCombo>> partials){
            List<Combo> combos = new ArrayList<>();
            for(ArrayList<PartialCombo> p: partials){
                combos.add(fromPartialToCombo(p));
            }
            return combos;
        }

        private List<String> movesToString(List<Combo> combos){
            List<String> strings = new ArrayList<>();
            for (Combo c: combos)
                strings.add(c.getName());
            return strings;
        }

        private List<String> fromPartialToStringCombo(List<ArrayList<PartialCombo>> partials){
            return movesToString(convertMoves(partials));
        }

    @Override
    public void dispatch(ChosenComboEvent message) {
        disablePowerUps(currentPlayer,"onAttack");
        comboIndex = 0;
        currentCombo = (Combo) model.getComboHelper().findByName(message.getChosenCombo());
        currentCombo.getPartialCombos().get(comboIndex).use(model, message.getSource());

    }

    @Override
    public void dispatch(VCEndOfTurnEvent message) {
        if (finalFrenzyTurn) {
            frenzyTurnCounter++;
            if (frenzyTurnCounter == model.getPlayers().size())
                return;
        }
        turnTimer.endTimer();
    }

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

    @Override
    public void dispatch(GrabEvent message) {
        model.userToPlayer(message.getSource()).grabStuff(message.getGrabbed());
        nextPartialCombo();
    }

    private void nextCombo(){
        comboUsed++;
        if (comboUsed == 2) {
            model.unloadedWeapons(currentPlayer);
            return;
        }
        model.send(new TurnEvent(currentPlayer, fromPartialToStringCombo(getAllowedMoves())));
        model.usablePowerUps("onTurn", false, model.userToPlayer(currentPlayer));
    }

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

    private Set<PartialCombo> getSetCombo(){
        Set<PartialCombo> partials= new HashSet<>();
        for(int j = comboIndex; j < currentCombo.getPartialCombos().size(); j++)
            partials.add(currentCombo.getPartialCombos().get(comboIndex));
        return partials;
    }

    @Override
    public void dispatch(DiscardedPowerUpEvent message) {
        model.userToPlayer(message.getSource()).discardPowerUp(message.getDiscardedPowerUp());
    }

    @Override
    public void dispatch(VCCardEndEvent message) {
        nextPartialCombo();
    }

    @Override
    public void dispatch(CalculatePointsEvent message) {
        model.frenzyUpdatePlayerStatus(model.userToPlayer(message.getSource()));
    }

    private void run (String username, Point destination, int distance){
        model.userToPlayer(username).run(destination, distance);
    }

    private void reloadWeapon (String username, String weaponName){
        model.userToPlayer(username).reload(model.nameToWeapon(weaponName));
    }

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

    //Create an event to assure that whenever a player leaves he forces spawn in a point
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

    public int getComboUsed() {
        return comboUsed;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

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
}
