package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.DefineTeleportPositionEvent;

public class TurnController extends Controller {
    private Dispatcher dispatcher;

    public TurnController (Game model, Server server){
        super(model, server);
        server.addController(this);
    }

    @Override
    public void update(VCEvent message) {
        message.handle(dispatcher);
    }

    private class Dispatcher extends VCEventDispatcher{
        @Override
        public void update(DefineTeleportPositionEvent message) {
            model.movePlayer(message.getSource(), message.getTeleportPosition());
        }
    }

}
