package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Connection;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.*;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvents.DisconnectionEvent;
import it.polimi.se2019.view.VCEvents.JoinEvent;

import java.util.List;


public class MatchController extends Controller{
    private List<String> usernames;
    private Dispatcher dispatcher;

    public MatchController(Game model, Server server){
        super(model, server);
        startMatch();
    }

    private void startMatch(){
        model.startMatch();
    }

    @Override
    public void update(VCEvent message) {
        dispatcher.update(message);
    }

    private class Dispatcher extends VCEventDispatcher {
        @Override
        public void update(VCEvent message) {
            try {
                message.handle(this);
            } catch (UnsupportedOperationException e) {
                Log.severe(e.getMessage());
            }
        }

        public void update(DisconnectionEvent message) {
            Log.info("Client " + message.getSource() + " just disconnected");
            model.pausePlayer(message.getSource());
        }

        public void update(JoinEvent message) {
            model.unpausePlayer(message.getSource());
        }

    }

}
