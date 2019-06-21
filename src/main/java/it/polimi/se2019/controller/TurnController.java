package it.polimi.se2019.controller;

import it.polimi.se2019.model.AmmoColour;
import it.polimi.se2019.model.Combo;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.mv_events.EndOfTurnEvent;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.Event;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.*;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class TurnController extends Controller {
    private ArrayList<String> effects= new ArrayList<>();
    private ArrayList<ArrayList<String>> targets= new ArrayList<>();
    private boolean isFirstTurn= true; //TODO: set to false after first turn
    private String currentPlayer;
    private Combo currentCombo;
    private int comboIndex=-1;
    private int comboUsed= -1;

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
        public void dispatch(ReloadEvent message) {
            model.reloadWeapon(message.getSource(), message.getWeaponName());
        }

        @Override
        public void dispatch(VCMoveEvent message) {
            if (message.getIsTeleport()){model.teleportPlayer(message.getSource(), message.getDestination()); }
             else {
                 model.run(message.getSource(), message.getDestination());
             }
        }

        @Override
        public void dispatch(GrabEvent message) {
            model.grab(message.getSource(), message.getGrabbed());
        }

        @Override
        public void dispatch(SpawnEvent message) {
            try {
                model.spawn(message.getSource(), stringToAmmo(message.getDiscardedPowerUpColour()), message.getPowerUpToKeep());
            } catch (NullPointerException e) {
                Log.severe(e.getMessage());
            }
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
        model.discardPowerUp(message.getSource(), message.getDiscardedPowerUp());
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

}
