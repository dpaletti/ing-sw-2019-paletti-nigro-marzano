package it.polimi.se2019.controller;

import it.polimi.se2019.model.FigureColour;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvent;

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

    private void finalFrenzyPointCalculation (String user){
        Map<FigureColour, Integer> figuresToHits;   //map of shooters to number of hits
        for (Player p: model.getPlayers()){
            figuresToHits= calculateHits(p.getHp());
            assignPoints (figuresToHits, user, p.getHp());
        }
    }
}
