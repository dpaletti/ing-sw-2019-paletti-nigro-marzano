package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.DefineTeleportPositionEvent;

public class TurnController extends Controller {
    private Dispatcher dispatcher = new Dispatcher(); //care not to leave this uninitialized

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
            model.movePlayer(message.getSource(), message.getTeleportPosition());
        }
    }

}
