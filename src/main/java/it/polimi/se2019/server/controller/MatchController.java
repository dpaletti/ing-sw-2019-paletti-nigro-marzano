package it.polimi.se2019.server.controller;

import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.mv_events.MvJoinEvent;
import it.polimi.se2019.commons.utility.JsonHandler;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.vc_events.DisconnectionEvent;
import it.polimi.se2019.commons.vc_events.VcJoinEvent;
import it.polimi.se2019.commons.vc_events.VcReconnectionEvent;
import it.polimi.se2019.server.model.Game;
import it.polimi.se2019.server.network.Server;

import java.util.List;

/**
 * This class handles the disconnection and reconnection of a user from the match pausing and unpausing the players to
 * allow the game to continue. See {@link it.polimi.se2019.server.controller.Controller}.
 */

public class MatchController extends Controller {

    public MatchController(Game model, Server server, List<String> usernames, int roomNumber){
        super(model, server, roomNumber);
    }


    @Override
    public void update(VCEvent message) {
        if(disabled)
            return;
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //ignore unsupported events
            Log.fine("MatchController ignored: " + JsonHandler.serialize(message));
        }
    }


        @Override
        public void dispatch(DisconnectionEvent message) {
            Log.fine(message.getSource() + "just disconnected");
            model.pausePlayer(message.getSource());
        }

        @Override
        public void dispatch(VcReconnectionEvent message) {
            Log.fine("Handling " + message);
            model.unpausePlayer(message.getUsername());
        }

    @Override
    public void dispatch(VcJoinEvent message) {
        model.send(new MvJoinEvent("*", message.getUsername(), 0));
    }
}

