package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PowerUp;
import it.polimi.se2019.model.Tile;
import it.polimi.se2019.model.mv_events.StartFirstTurnEvent;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.DisconnectionEvent;
import it.polimi.se2019.view.vc_events.VcReconnectionEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MatchController extends Controller {
    private List<String> activeUsernames;

    public MatchController(Game model, Server server, List<String> usernames, int roomNumber){
        super(model, server, roomNumber);
        this.activeUsernames = usernames;
        startMatch();
    }

    private void startMatch(){
        List<Tile> spawnTiles = model.getGameMap().getSpawnTiles();
        Map<Point, String> spawnPoints= new HashMap<>();
        for(Tile t: spawnTiles)
            spawnPoints.put(t.getPosition(), t.getColour().toString());
        model.send(new StartFirstTurnEvent(model.playerToUser(model.getPlayers().get(0)),
                ((PowerUp)model.getPowerUpDeck().draw()).getName(),
                ((PowerUp)model.getPowerUpDeck().draw()).getName(),
                true, spawnPoints));
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
}
