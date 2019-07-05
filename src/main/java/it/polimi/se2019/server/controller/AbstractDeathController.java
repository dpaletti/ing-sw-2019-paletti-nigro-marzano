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

/**
 * This class implements some useful methods used when calculating points.
 */

public abstract class AbstractDeathController extends Controller {

    public AbstractDeathController(Game model, Server server, int roomNumber) {
        super(model, server, roomNumber);
    }

    public AbstractDeathController(){
        //Empty constructor for testing
    }

    /**
     * This method ignores the events that are not dispatched in this controller.
     * @param message Any message arriving from the view.
     */

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

    /**
     * This method assigns the points to the users when a player dies or when the match ends.
     * @param figuresToHits a map between the figure colour and the number of hits they gave to the dead user.
     * @param user the user that died.
     * @param hp the hps of the dead user.
     */

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

    /**
     * Calculates the hits each player gave to the player that died.
     * @param hp the hps of the dead pplayer.
     * @return the map between the colour of the shooter and the number of hits they caused.
     */

    protected Map <FigureColour, Integer> calculateHits (List<Tear> hp){  //assigns number of hits to the player that caused them
        Map<FigureColour, Integer> leaderboard= new HashMap<>();
        for (Tear tear: hp){
            if (!leaderboard.containsKey(tear.getColour()))
                leaderboard.put(tear.getColour(), 0);
            leaderboard.put(tear.getColour(), leaderboard.get(tear.getColour())+1);
        }
        return leaderboard;
    }

    /**
     * When two player tie, this method assigns the points to the user that hit first among those tying.
     * @param pointsToAssign the points left to assign.
     * @param tyingFigures the figures who gave the same number of hits.
     * @param hp the hps of the dead player.
     */
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

    /**
     * Gives one extra point to the user that shot first.
     * @param firstShooter the user that shot first.
     */

    protected void firstBlood (Tear firstShooter){
        updatePoints(model.colourToUser(firstShooter.getColour()), 1);
    }

    /**
     * Calculates the overkills caused during a match.
     * @param killshotTrack the kills that took place during the match.
     */
    protected void calculateOverkills (List<Skull> killshotTrack){
        for (Skull s: killshotTrack){
            if (s.getOverkill())
                updatePoints(model.colourToUser(s.getTear().getColour()), 1);
        }
    }

    /**
     * calculates points at the end of the match.
     * @param killshotTrack the kills that took place during the match.
     */

    //8, 6, 4, 2, 1, 1, overkills give a +1 and ties are solved in favour of the first to kill
    protected void winnerPointCalculation (List<Skull> killshotTrack){
        List<Tear> killshot= new ArrayList<>();
        for (Skull s: killshotTrack)
            killshot.add(s.getTear());
        Map<FigureColour, Integer> figuresToKills = calculateHits(killshot);
        assignPoints(figuresToKills, "", killshot);
        calculateOverkills(killshotTrack);
    }

    /**
     * Updates points of users when calculating the scores.
     * @param username user gaining points.
     * @param points points they gained.
     */

    private void updatePoints (String username, int points){
        model.userToPlayer(username).setPoints(model.userToPlayer(username).getPoints()+points);
        model.send(new UpdatePointsEvent(username,"*" , model.userToPlayer(username).getPoints()));
    }

    /**
     * Returns points to assign in the different phases of the match.
     * @param username username of the player that died.
     * @return a list of points to assign.
     */

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

    /**
     * Implements the end of he match calculating points and notifying all users.
     */

    public void endOfMatch(){
        winnerPointCalculation(model.getKillshotTrack().getKillshot());
        HashMap<String, Integer> points = new HashMap<>();
        for (Player p : model.getPlayers())
            points.put(model.playerToUser(p), p.getPoints());
        model.send(new EndOfMatchEvent("*", points));
    }
}
