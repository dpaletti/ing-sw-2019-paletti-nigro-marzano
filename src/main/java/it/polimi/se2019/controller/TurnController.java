package it.polimi.se2019.controller;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.mv_events.MVMoveEvent;
import it.polimi.se2019.model.mv_events.NotEnoughPlayersConnectedEvent;
import it.polimi.se2019.model.mv_events.StartFirstTurnEvent;
import it.polimi.se2019.model.mv_events.TurnEvent;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.PartialCombo;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TurnController extends Controller {
    private ArrayList<String> effects= new ArrayList<>();
    private ArrayList<ArrayList<String>> targets= new ArrayList<>();
    private boolean isFirstTurn= true; //TODO: set to false after first turn
    private String currentPlayer;
    private Combo currentCombo;
    private int comboIndex= 0;
    private int comboUsed= 0;
    private boolean reloaded = false;
    private int turnCounter=0;

    @Override
    protected void endTimer() {
        super.endTimer();
        endTurn();
    }

    //should if(currentCombo.getPartialCombos().get(comboIndex).equals(PartialCombo.WHATEVER)) be checked for Move, grab and shoot as well as reload?

    public TurnController (Game model, Server server, int roomNumber){
        super(model, server, roomNumber);
        currentPlayer = model.getUsernames().get(0);
        //turn controller is registered to virtualView in closeMatchMaking() inside MatchMaking controller
        //either leave things like this or take that one out and add server.addController(this) here
    }

    public TurnController (Game game){
        this.model = game;
        currentPlayer = model.getUsernames().get(0);
    }

    @Override
    public void update(VCEvent message) {
        try {
            if(message.getSource().equals(currentPlayer))
                message.handle(this);
        }catch (UnsupportedOperationException e){
            //ignore events that this controller does not support
            Log.fine("TurnController ignored " + JsonHandler.serialize(message));
        }
    }

    @Override
    public void dispatch(SpawnEvent message) {
        if (!message.getPowerUpToKeep().equals("")) {
            spawn(message.getSource(), stringToAmmo(message.getDiscardedPowerUpColour()), message.getPowerUpToKeep()); }
    }

    @Override
    public void dispatch(ReloadEvent message) {
        if(currentCombo == null) {
            reloaded = true;
            reloadWeapon(message.getSource(), message.getWeaponName());
        }
        else if (currentCombo.getPartialCombos().get(comboIndex).equals(PartialCombo.RELOAD)) {
            reloadWeapon(message.getSource(), message.getWeaponName());
            nextPartialCombo();
        }
        if (comboUsed==2 || reloaded)
            endTurn();
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
        comboIndex=0;
        currentCombo = (Combo) model.getComboHelper().findByName(message.getChosenCombo());
        currentCombo.getPartialCombos().get(comboIndex).use(model, message.getSource());

    }

    @Override
    public void dispatch(EndOfTurnEvent message) {
        endTurn();
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
        if (comboUsed==2) {
            model.unloadedWeapons(currentPlayer);
            return;
        }
        model.send(new TurnEvent(currentPlayer, fromPartialToStringCombo(model.userToPlayer(currentPlayer).getHealthState().getMoves())));
        model.usablePowerUps("onTurn", false, model.userToPlayer(currentPlayer));
    }

    private void nextPartialCombo (){
        comboIndex++;
        if (comboIndex < currentCombo.getPartialCombos().size())
            currentCombo.getPartialCombos().get(comboIndex).use(model,currentPlayer);
        if (comboIndex == currentCombo.getPartialCombos().size())
            nextCombo();
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
        nextPartialCombo();
    }

    @Override
    public void dispatch(CalculatePointsEvent message) {
        if (model.isFinalFrenzy())
            new FinalFrenzyController(server, getRoomNumber(), model, this);
    }

    private AmmoColour stringToAmmo (String ammoName){
        for (AmmoColour ammoColour: AmmoColour.values()){
            if (ammoColour.toString().equalsIgnoreCase(ammoName)){
                return ammoColour;
            }
        }
        throw new NullPointerException("This ammo doesn't exist");
    }

    private void run (String username, Point destination, int distance){
        model.userToPlayer(username).run(destination, distance);
    }

    private void reloadWeapon (String username, String weaponName){
        model.userToPlayer(username).reload(model.nameToWeapon(weaponName));
    }

    private void spawn (String username, AmmoColour spawnColour, String powerUpName) {
        for (Tile tile : model.getGameMap().getSpawnTiles()) {
            if (tile.getColour().toString().equals(spawnColour.toString()))
                model.userToPlayer(username).getFigure().spawn(tile.getPosition());
        }
        if (model.nameToPowerUp(powerUpName) != null)
            model.userToPlayer(username).drawPowerUp(powerUpName);
        model.send(new MVMoveEvent("*", username, model.userToPlayer(username).getFigure().getPosition()));
        model.usablePowerUps("onTurn", false, model.userToPlayer(currentPlayer));
        model.send(new TurnEvent(username, fromPartialToStringCombo(model.userToPlayer(username).getHealthState().getMoves())));
    }

    private void endTurn(){
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
            else
                model.send(new TurnEvent(currentPlayer,fromPartialToStringCombo(model.userToPlayer(currentPlayer).getHealthState().getMoves())));
            //This line is commented out only to pass the serverless tests
            //startTimer(server.getTurnTimer());
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
}
