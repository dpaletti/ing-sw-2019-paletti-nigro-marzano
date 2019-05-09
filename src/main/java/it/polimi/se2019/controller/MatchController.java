package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.*;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.vc_events.DisconnectionEvent;
import it.polimi.se2019.view.vc_events.JoinEvent;

import java.util.List;


public class MatchController extends Controller{
    private List<String> usernames;
    private Dispatcher dispatcher = new Dispatcher();

    public MatchController(Game model, Server server){
        super(model, server);
        startMatch();
    }

    private void startMatch(){
        model.startMatch();
    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException("Match Controller dispatcher: " + e.getMessage(), e);
        }
    }

    private class Dispatcher extends VCEventDispatcher {

        @Override
        public void update(DisconnectionEvent message) {
            model.pausePlayer(message.getSource());
        }

        @Override
        public void update(JoinEvent message) {
            model.unpausePlayer(message.getSource());
        }

    }

}
