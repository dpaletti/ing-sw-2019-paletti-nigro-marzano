package it.polimi.se2019.controller;

import it.polimi.se2019.model.AmmoColour;
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
    private Semaphore sem = new Semaphore(1, true);
    private ArrayList<String> effects= new ArrayList<>();
    private ArrayList<ArrayList<String>> targets= new ArrayList<>();

    public class TargetListener implements Runnable{

        @Override
        public void run() {
            try {
                sem.acquire();
            } catch (InterruptedException e){
                Log.severe(e.getMessage());
                Thread.currentThread().interrupt();
            }

        }
    }
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
        public void dispatch(DefineTeleportPositionEvent message) {
            model.teleportPlayer(message.getSource(), message.getTeleportPosition());
        }

        @Override
        public void dispatch(ReloadEvent message) {
            model.reloadWeapon(message.getSource(), message.getWeaponName());
        }

        @Override
        public void dispatch(VCMoveEvent message) {
            model.run(message.getSource(), message.getDestination());
        }

        @Override
        public void dispatch(GrabEvent message) {
            model.grab(message.getSource(), message.getGrabbed());
        }

        @Override
        public void dispatch(ChosenTargetAndEffectEvent message) {
            effects= message.getEffectNames();
            targets= message.getTargetNames();
            sem.release();
        }

        @Override
        public void dispatch(ChosenWeaponEvent message) {
            ArrayList<String> effect= new ArrayList<>(); //acquired from ChosenEffectEvent
            ArrayList<ArrayList<String>> target= new ArrayList<>(); //acquired from ChosenTargetAndEffectEvent
            Thread listener = new Thread(new TargetListener());
            listener.start();
            try {
                sem.acquire();
            } catch (InterruptedException e){
                Log.severe(e.getMessage());
                Thread.currentThread().interrupt();
            }
            model.shoot(message.getSource(), message.getWeapon(), effect, target);
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
            if (message.getAction().equalsIgnoreCase("runAround")) model.allowedMovements(message.getSource(), 3);
            else if (message.getAction().equalsIgnoreCase("moveAndGrab")) model.allowedMovements(message.getSource(), 1);
            else if (message.getAction().equalsIgnoreCase("shootPeople")) model.allowedWeapons(message.getSource());
            else if (message.getAction().equalsIgnoreCase("moveMoveGrab")) model.allowedMovements(message.getSource(), 2);
            else if (message.getAction().equalsIgnoreCase("moveAndShoot")) model.allowedMovements(message.getSource(), 1);
            else if (message.getAction().equalsIgnoreCase("frenzyMoveReloadShoot")) model.allowedMovements(message.getSource(), 1);
            else if (message.getAction().equalsIgnoreCase("moveFourSquares")) model.allowedMovements(message.getSource(), 4);
            else if (message.getAction().equalsIgnoreCase("frenzyMoveAndGrab")) model.allowedMovements(message.getSource(), 1);
            else if (message.getAction().equalsIgnoreCase("moveTwiceGrabShoot")) model.allowedMovements(message.getSource(), 2);
            else if (message.getAction().equalsIgnoreCase("moveThriceAndShoot")) model.allowedMovements(message.getSource(), 3);
        }

        @Override
        public void dispatch(ChosenEffectEvent message) {
            //TODO: method to calculate possible targets
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
    }

    private AmmoColour stringToAmmo (String ammoName){
            for (AmmoColour ammoColour: AmmoColour.values()){
                if (ammoColour.toString().equalsIgnoreCase(ammoName)){
                    return ammoColour;
                }
            }
            throw new NullPointerException("This ammo doesn't exist");
        }

}
