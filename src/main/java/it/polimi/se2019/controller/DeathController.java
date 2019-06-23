package it.polimi.se2019.controller;

import it.polimi.se2019.model.*;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.*;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.*;

import java.util.*;

public class DeathController extends AbstractDeathController{

    public DeathController(Server server, int roomNumber, Game model){
        super(model, server, roomNumber);
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
    public void dispatch(SpawnEvent message) {
        List<Tear> hp= model.getHp(message.getSource());
        if (hp.size()<11)
            return;
        deathPointCalculation(message.getSource(), hp);
        model.userToPlayer(message.getSource()).emptyHp();
    }

    @Override
    public void dispatch(CalculatePointsEvent message) {
        for (Player p: model.getPlayers())
            deathPointCalculation(message.getSource(), model.getHp(model.playerToUser(p)));
        if (model.isFinalFrenzy()){
            new FinalFrenzyDeathController(server, getRoomNumber(), model, this);
            return;
        }
        winnerPointCalculation(message.getSource(), model.getKillshotTrack().getKillshot());
    }

    boolean overkill (List<Tear> hp){
        return hp.size()==12;
    }

     void deathPointCalculation (String user, List<Tear> hp){
        Map<FigureColour, Integer> figuresToHits = calculateHits(hp);   //map of shooters to number of hits
        firstBlood(hp.get(0));  //player causing first blood obtains 1 extra point
        assignPoints (figuresToHits, user, hp);
    }


}
