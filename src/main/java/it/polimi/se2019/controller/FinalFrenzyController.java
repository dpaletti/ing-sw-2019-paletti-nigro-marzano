package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvent;

//TurnController will be deleted upon creation of FinalFrenzyController


public class FinalFrenzyController extends AbstractTurnController {
    public FinalFrenzyController(Server server, int roomNumber, Game model, TurnController turnController){
        super(model, server, roomNumber);
        server.removeController(turnController, roomNumber);
    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //ignore events that this controller does not support
            Log.fine("DeathController ignored " + JsonHandler.serialize(message));
        }
    }



}
