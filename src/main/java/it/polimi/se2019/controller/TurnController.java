package it.polimi.se2019.controller;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.PartialCombo;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.*;
import it.polimi.se2019.view.vc_events.DiscardedPowerUpEvent;

import java.util.*;

public class TurnController extends Controller {
    private ArrayList<String> effects = new ArrayList<>();
    private ArrayList<ArrayList<String>> targets = new ArrayList<>();
    private boolean isFirstTurn = true; //TODO: set to false after first turn
    private String currentPlayer;
    private Combo currentCombo;
    private int comboIndex = 0;
    private int comboUsed = 0;
    private boolean reloaded = false;
    private int turnCounter=0;
    private TickingTimer interTurnTimer;
    private TickingTimer turnTimer;

    //should if(currentCombo.getPartialCombos().get(comboIndex).equals(PartialCombo.WHATEVER)) be checked for Move, grab and shoot as well as reload?

    public TurnController (Game model, Server server, int roomNumber){
        super(model, server, roomNumber);
        currentPlayer = model.getUsernames().get(0);
        turnTimer= new TickingTimer(model, this::endTurn);
        interTurnTimer= new TickingTimer(model, this::endTurn);
        //turn controller is registered to virtualView in closeMatchMaking() inside MatchMaking controller
        //either leave things like this or take that one out and add server.addController(this) here
    }

    //Test constructor
    public TurnController (Game game,Server server){
        this.model = game;
        currentPlayer = model.getUsernames().get(0);
        this.server=server;
        turnTimer= new TickingTimer(model, this::endTurn);
        interTurnTimer= new TickingTimer(model, this::endTurn);
    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //ignore events that this controller does not support
            Log.fine("TurnController ignored " + JsonHandler.serialize(message));
        }
    }

    @Override
    public void dispatch(SpawnEvent message) {
        boolean isRespawn = false;
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
            throw new IllegalArgumentException("No combo exist with such partialCombos");
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
        comboIndex = 0;
        currentCombo = (Combo) model.getComboHelper().findByName(message.getChosenCombo());
        currentCombo.getPartialCombos().get(comboIndex).use(model, message.getSource());

    }

    @Override
    public void dispatch(VCEndOfTurnEvent message) {
        turnTimer.endTimer();
    }

    @Override
    public void dispatch(VCMoveEvent message) {
        int distance = 1;
        if (message.getIsTeleport())
            distance = -1;
        run(message.getSource(), message.getDestination(), distance);
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
    public void dispatch(DisconnectionEvent message) {
        model.pausePlayer(message.getSource());
        if (!enoughActivePlayers())
            model.send(new NotEnoughPlayersConnectedEvent("*"));
    }

    @Override
    public void dispatch(VcReconnectionEvent message) {
        model.unpausePlayer(message.getUsername());
    }

    @Override
    public void dispatch(DiscardedPowerUpEvent message) {
        model.userToPlayer(message.getSource()).discardPowerUp(message.getDiscardedPowerUp());
    }

    @Override
    public void dispatch(VCWeaponEndEvent message) {
        disablePowerUps(currentPlayer,"onAttack");
        nextPartialCombo();
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
        String previouslyPlaying = currentPlayer;
        comboUsed = 0;
        comboIndex = 0;
        currentCombo = null;
        turnCounter++;
        if (enoughActivePlayers()){
            currentPlayer = getNextActiveUser(currentPlayer);
            if (turnCounter== model.getUsernames().size())
                isFirstTurn=false;
            if (isFirstTurn)
                model.send(new StartFirstTurnEvent(currentPlayer,
                        model.getPowerUpDeck().draw().getName(),
                        model.getPowerUpDeck().draw().getName(),false,model.getGameMap().getMappedSpawnPoints()));
            else {
                refreshBoard();
                model.send(new MVEndOfTurnEvent("*", previouslyPlaying, currentPlayer));
                disablePowerUps(previouslyPlaying,"onTurn");
                model.send(new TurnEvent(currentPlayer,
                        fromPartialToStringCombo(getAllowedMoves())));
                turnTimer.startTimer(server.getTurnTimer());
            }
        }
        else
            model.send(new NotEnoughPlayersConnectedEvent("*"));
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
            for (String s : model.getPlayersWaitingToRespawn())
                model.send(new MVRespawnEvent(s, model.getPowerUpDeck().draw().getName()));
            interTurnTimer.startTimer(server.getInterTurnTimer());
        }
    }

    private void refreshBoard() {
        HashMap<Point, String> loot = new HashMap<>();
        HashMap<String, String> spawn = new HashMap<>();
        Drawable card;

        for (Point p : model.getEmptyLootTiles()) {
            card = model.getLootDeck().draw();
            loot.put(p, card.getName());
            model.getTile(p).add((LootCard)card);
        }
        for (Point p : model.getEmptySpawnTiles()) {
            card = model.getWeaponDeck().draw();
            spawn.put(card.getName(), model.getTile(p).getColour().name());
            model.getTile(p).add((Weapon)card);
        }

        model.send(new BoardRefreshEvent("*", spawn, loot));
    }

}
