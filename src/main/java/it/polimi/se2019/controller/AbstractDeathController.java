package it.polimi.se2019.controller;

import it.polimi.se2019.model.FigureColour;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Skull;
import it.polimi.se2019.model.Tear;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDeathController extends Controller {

    public AbstractDeathController(Game model, Server server, int roomNumber) {
        super(model, server, roomNumber);
    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //ignore events that this controller does not support
            Log.fine("AbstractDeathController ignored " + JsonHandler.serialize(message));
        }
    }

    protected void assignPoints (Map<FigureColour, Integer> figuresToHits, String user, List<Tear> hp){
        List<FigureColour> localBestFigures= new ArrayList<>(); //players who gave the same number of hits to dead player
        for (int i=0; i<model.getPlayers().size()-1; i++){
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

    protected Map <FigureColour, Integer> calculateHits (List<Tear> hp){  //assigns number of hits to the player that caused them
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

    protected void firstBlood (Tear firstShooter){
        model.updatePoints(model.colourToUser(firstShooter.getColour()), 1);
    }

    protected void calculateOverkills (List<Skull> killshotTrack){
        for (Skull s: killshotTrack){
            if (s.getOverkill())
                model.updatePoints(model.colourToUser(s.getTear().getColour()), 1);
        }
    }

    //8, 6, 4, 2, 1, 1, overkills give a +1 and ties are solved in favour of the first to kill
    protected void winnerPointCalculation (String user, List<Skull> killshotTrack){
        List<Tear> killshot= new ArrayList<>();
        for (Skull s: killshotTrack)
            killshot.add(s.getTear());
        Map<FigureColour, Integer> figuresToKills = calculateHits(killshot);
        assignPoints(figuresToKills, user, killshot);
        calculateOverkills(killshotTrack);
    }
}
