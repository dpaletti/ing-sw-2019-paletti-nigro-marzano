package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.*;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class TurnController extends Controller {
    private Dispatcher dispatcher;
    private Semaphore sem = new Semaphore(1, true);
    private ArrayList<String> effects= new ArrayList<>();
    private ArrayList<ArrayList<String>> targets= new ArrayList<>();

    public class TargetListener implements Runnable{
    private Dispatcher dispatcher = new Dispatcher(); //care not to leave this uninitialized

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
    public TurnController (Game model, Server server){
        super(model, server);
        //turn controller is registered to virtualView in closeMatchMaking() inside MatchMaking controller
        //either leave things like this or take that one out and add server.addController(this) here
    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            //ignore events that this controller does not support
            Log.fine("TurnController ignored " + JsonHandler.serialize(message));
        }
    }

    private class Dispatcher extends VCEventDispatcher{
        @Override
        public void update(DefineTeleportPositionEvent message) {
            model.teleportPlayer(message.getSource(), message.getTeleportPosition());
        }

        @Override
        public void update(ReloadEvent message) {
            model.reloadWeapon(message.getSource(), message.getWeaponName());
        }

        @Override
        public void update(MoveEvent message) {
            model.run (message.getSource(), message.getDestionation());
        }

        @Override
        public void update(GrabEvent message) {
            model.grab(message.getSource());
        }

        @Override
        public void update(ChosenTargetAndEffectEvent message) {
            effects= message.getEffectNames();
            targets= message.getTargetNames();
            sem.release();
        }

        @Override
        public void update(ChosenWeaponEvent message) {
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
    }

}
