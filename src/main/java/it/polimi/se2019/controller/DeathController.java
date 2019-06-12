package it.polimi.se2019.controller;

import it.polimi.se2019.model.FigureColour;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Tear;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.*;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.SpawnEvent;

import java.util.*;

public class DeathController extends Controller{

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
        assignPoints(message.getSource(), hp);
        model.userToPlayer(message.getSource()).setHp(null);
    }

    boolean overkill (List<Tear> hp){
        return hp.size()==12;
    }

    void assignPoints (String user, List<Tear> hp){
        Map<FigureColour, Integer> figuresToHits = calculateHits(hp);   //map of shooters to number of hits
        List<FigureColour> localBestFigures= new ArrayList<>(); //players who gave the same number of hits to dead player
        model.updatePoints(model.colourToUser(hp.get(0).getColour()), 1);   //player causing first blood obtains 1 extra point

        for (int i=0; i<4; i++){
            Integer localMaximum = 0;
            for (Map.Entry<FigureColour, Integer> entry : figuresToHits.entrySet()) {

                if (localMaximum == 0 || localMaximum < entry.getValue()) {
                    localMaximum = entry.getValue();
                    localBestFigures.clear();
                    localBestFigures.add(entry.getKey());
                }

                else if (localMaximum.equals(entry.getValue()))
                    localBestFigures.add(entry.getKey());
            }

            List<Integer> pointsToAssign= model.getPointsToAssign(user).subList(i, i+localBestFigures.size());
            i+=(localBestFigures.size()-1);
            for (FigureColour f: localBestFigures) {
                figuresToHits.remove(f);
            }
            solveTies(pointsToAssign, localBestFigures, hp);
            localBestFigures.clear();
        }
    }

    Map <FigureColour, Integer> calculateHits (List<Tear> hp){  //assigns number of hits to the player that caused them
        Map<FigureColour, Integer> leaderboard= new HashMap<>();
        for (Tear tear: hp){
            if (!leaderboard.containsKey(tear.getColour()))
                leaderboard.put(tear.getColour(), 0);
            leaderboard.put(tear.getColour(), leaderboard.get(tear.getColour())+1);
        }
        return leaderboard;
    }

    void solveTies (List<Integer> pointsToAssign, List<FigureColour> tyingFigures, List<Tear> hp){
        int counter = 0;
        for (Tear t: hp){
            if (tyingFigures.contains(t.getColour())){
                model.updatePoints(model.colourToUser(t.getColour()), pointsToAssign.get(counter));
                tyingFigures.remove(t.getColour());
                counter++;
            }
        }
    }
}
