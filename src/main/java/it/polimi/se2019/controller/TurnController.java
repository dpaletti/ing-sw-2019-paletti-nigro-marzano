package it.polimi.se2019.controller;

import it.polimi.se2019.model.AmmoColour;
import it.polimi.se2019.model.Combo;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.mv_events.NotEnoughPlayersConnectedEvent;
import it.polimi.se2019.utility.PartialCombo;
import it.polimi.se2019.view.vc_events.EndOfTurnEvent;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.mv_events.MVMoveEvent;
import it.polimi.se2019.model.mv_events.TurnEvent;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.*;

import java.util.ArrayList;

public class TurnController extends Controller {
    private ArrayList<String> effects= new ArrayList<>();
    private ArrayList<ArrayList<String>> targets= new ArrayList<>();
    private boolean isFirstTurn= true; //TODO: set to false after first turn
    private String currentPlayer = model.getUsernames().get(0);
    private Combo currentCombo;
    private int comboIndex=-1;
    private int comboUsed= -1;


    //model.send (new TurnEvent(model.playerToUser(currentPlayer)));

    public TurnController (Game model, Server server, int roomNumber){
        super(model, server, roomNumber);
        //turn controller is registered to virtualView in closeMatchMaking() inside MatchMaking controller
        //either leave things like this or take that one out and add server.addController(this) here
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
        if (!message.getPowerUpToKeep().equals("")) {
            spawn(message.getSource(), stringToAmmo(message.getDiscardedPowerUpColour()), message.getPowerUpToKeep()); }
    }

    @Override
    public void dispatch(ReloadEvent message) {
            reloadWeapon(message.getSource(), message.getWeaponName());
        }

    @Override
    public void dispatch(ChosenComboEvent message) {
        for (PartialCombo p : message.getChosenCombo())
            p.use(model, message.getSource());
        comboUsed++;
        if (comboUsed < 2)
            model.send(new TurnEvent(message.getSource(), model.userToPlayer(message.getSource()).getHealthState().getMoves()));
        else
            endTurn();
    }
        @Override
        public void dispatch(VCMoveEvent message) {
        int distance = 3;
            if (message.getIsTeleport())
                distance = -1;
             run(message.getSource(), message.getDestination(), distance);
        }

    @Override
        public void dispatch(GrabEvent message) {
            model.userToPlayer(message.getSource()).grabStuff(message.getGrabbed());
        }


        @Override
        public void dispatch(PowerUpUsageEvent message) {
            try {
                model.usePowerUp(message.getSource(), message.getUsedPowerUp());
            } catch (NullPointerException e){
                Log.severe(e.getMessage());
            }
        }

        @Override
        public void dispatch(ActionEvent message) {
            currentCombo= new Combo(message.getAction());

        }

    @Override
    public void dispatch(DiscardedPowerUpEvent message) {
        model.userToPlayer(message.getSource()).discardPowerUp(message.getDiscardedPowerUp());
    }

    @Override
    public void dispatch(DisconnectionEvent message) {
        model.pausePlayer(message.getSource());
    }

    @Override
    public void dispatch(VcReconnectionEvent message) {
        model.unpausePlayer(message.getUsername());
    }

    @Override
    public void dispatch(EndOfTurnEvent message) {
        model.endTurn(message.getSource());
        //TODO: move to next player
        //update current player and counters
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

    private void nextCombo(){
        comboUsed++;
        if (comboUsed==2) {//TODO: settings
            //TODO: reload, end turn or use powerup
            //to end turn: model.endTurn(player) and currentPlayer= next
            return;
        }
        //TODO: send MVEvent to ask the user the next chosen combo
    }

    private void nextPartialCombo (){
        comboIndex++;
        if (comboIndex==currentCombo.getPartialCombos().size()){
            nextCombo();
            return;
        }
        currentCombo.getPartialCombos().get(comboIndex).use(model, currentPlayer);
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
                model.userToPlayer(username).run(tile.getPosition(), -1);
        }
        if (model.nameToPowerUp(powerUpName) != null)
            model.userToPlayer(username).drawPowerUp(powerUpName);
        model.send(new MVMoveEvent("*", username, model.userToPlayer(username).getFigure().getPosition()));
        model.send(new TurnEvent(username, model.userToPlayer(username).getHealthState().getMoves()));
    }

    private void endTurn(){
        comboUsed = -1;
        comboIndex = -1;

        if (enoughActivePlayers())
            currentPlayer = getNextActiveUser(currentPlayer);
        else
            model.send(new NotEnoughPlayersConnectedEvent("*"));
    }

    private boolean enoughActivePlayers (){
        int active = 0;
        for (Player p : model.getPlayers())
            active++;
        return !(active < 3);
    }

    private String getNextActiveUser (String user){
        if (model.userToPlayer(model.getUsernames().get(model.getUsernames().indexOf(user) + 1)).isPaused())
            return getNextActiveUser(model.getUsernames().get(model.getUsernames().indexOf(user) + 1));
        else
            return model.getUsernames().get(model.getUsernames().indexOf(user) + 1);
    }
}
