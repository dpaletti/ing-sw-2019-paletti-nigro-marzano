package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvent;

public abstract class AbstractTurnController extends Controller {

    //AbstractTurnController should create FF and Turn Controllers
    public AbstractTurnController (Game model, Server server, int roomNumber){
        super(model, server, roomNumber);
    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //ignore events that this controller does not support
            Log.fine("AbstractTurnController ignored " + JsonHandler.serialize(message));
        }
    }



}
