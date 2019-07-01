package it.polimi.se2019.server.controller;

import it.polimi.se2019.server.model.FigureColour;
import it.polimi.se2019.server.model.Game;
import it.polimi.se2019.server.model.Player;
import it.polimi.se2019.server.network.Server;
import it.polimi.se2019.commons.utility.JsonHandler;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.vc_events.VCEndOfTurnEvent;

import java.util.Map;

public class FinalFrenzyDeathController extends AbstractDeathController {

    public FinalFrenzyDeathController(Server server, int roomNumber, Game model, DeathController deathController){
        super(model, server, roomNumber);
        server.removeController(deathController, roomNumber);
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

    @Override
    public void dispatch(VCEndOfTurnEvent message) {
        finalFrenzyPointCalculation(message.getSource());
        winnerPointCalculation(model.getKillshotTrack().getKillshot());
    }

    private void finalFrenzyPointCalculation (String user){
        Map<FigureColour, Integer> figuresToHits;   //map of shooters to number of hits
        for (Player p: model.getPlayers()){
            figuresToHits= calculateHits(p.getHp());
            assignPoints (figuresToHits, user, p.getHp());
        }
    }
}
