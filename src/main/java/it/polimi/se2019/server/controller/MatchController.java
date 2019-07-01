package it.polimi.se2019.server.controller;

import it.polimi.se2019.commons.mv_events.MvJoinEvent;
import it.polimi.se2019.commons.vc_events.VcJoinEvent;
import it.polimi.se2019.server.model.Game;
import it.polimi.se2019.server.model.PowerUp;
import it.polimi.se2019.commons.mv_events.StartFirstTurnEvent;
import it.polimi.se2019.server.network.Server;
import it.polimi.se2019.commons.utility.JsonHandler;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.vc_events.DisconnectionEvent;
import it.polimi.se2019.commons.vc_events.VcReconnectionEvent;

import java.util.List;


public class MatchController extends Controller {
    private List<String> activeUsernames;

    public MatchController(Game model, Server server, List<String> usernames, int roomNumber){
        super(model, server, roomNumber);
        this.activeUsernames = usernames;
        startMatch();
    }

    private void startMatch(){
        model.send(new StartFirstTurnEvent(model.playerToUser(model.getPlayers().get(0)),
                ((PowerUp)model.getPowerUpDeck().draw()).getName(),
                ((PowerUp)model.getPowerUpDeck().draw()).getName(),
                true, model.getGameMap().getMappedSpawnPoints()));
    }

    @Override
    public void update(VCEvent message) {
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
        model.send(new MvJoinEvent("*", message.getUsername()));
    }
}

