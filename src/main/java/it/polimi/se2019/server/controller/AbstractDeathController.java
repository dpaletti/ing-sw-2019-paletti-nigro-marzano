package it.polimi.se2019.server.controller;

import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.mv_events.EndOfMatchEvent;
import it.polimi.se2019.commons.mv_events.UpdatePointsEvent;
import it.polimi.se2019.commons.utility.JsonHandler;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.server.model.*;
import it.polimi.se2019.server.network.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDeathController extends Controller {

    public AbstractDeathController(Game model, Server server, int roomNumber) {
        super(model, server, roomNumber);
    }

    public AbstractDeathController(){
        //Empty constructor for testing
    }
    @Override
    public void update(VCEvent message) {
        if(disabled)
            return;
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

            List<Integer> pointsToAssign= getPointsToAssign(user).subList(i, i+localBestFigures.size());
            if(!localBestFigures.isEmpty())
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
                updatePoints(model.colourToUser(t.getColour()), pointsToAssign.get(counter));
                tyingFigures.remove(t.getColour());
                counter++;
            }
        }
    }

    protected void firstBlood (Tear firstShooter){
        updatePoints(model.colourToUser(firstShooter.getColour()), 1);
    }

    protected void calculateOverkills (List<Skull> killshotTrack){
        for (Skull s: killshotTrack){
            if (s.getOverkill())
                updatePoints(model.colourToUser(s.getTear().getColour()), 1);
        }
    }

    //8, 6, 4, 2, 1, 1, overkills give a +1 and ties are solved in favour of the first to kill
    protected void winnerPointCalculation (List<Skull> killshotTrack){
        List<Tear> killshot= new ArrayList<>();
        for (Skull s: killshotTrack)
            killshot.add(s.getTear());
        Map<FigureColour, Integer> figuresToKills = calculateHits(killshot);
        assignPoints(figuresToKills, "", killshot);
        calculateOverkills(killshotTrack);
    }

    private void updatePoints (String username, int points){
        model.userToPlayer(username).setPoints(model.userToPlayer(username).getPoints()+points);
        model.send(new UpdatePointsEvent(username,"*" , model.userToPlayer(username).getPoints()));
    }

    private List<Integer> getPointsToAssign (String username){
        if (username.equalsIgnoreCase(""))
            return model.getPointsToAssign();
        Player player = model.userToPlayer(username);
        List<Integer> points;
        //checks whether player is in FinalFrenzy
        if (player.getHealthState().isFinalFrenzy())
            points = model.getFrenzyPointsToAssign();
        else
            points = model.getPointsToAssign();
        int index = points.indexOf(player.getPlayerValue().getMaxValue());
        return new ArrayList<>(model.getPointsToAssign().subList(index, model.getPointsToAssign().size()));
    }

    public void endOfMatch(){
        winnerPointCalculation(model.getKillshotTrack().getKillshot());
        HashMap<String, Integer> points = new HashMap<>();
        for (Player p : model.getPlayers())
            points.put(model.playerToUser(p), p.getPoints());
        model.send(new EndOfMatchEvent("*", points));
    }
}
