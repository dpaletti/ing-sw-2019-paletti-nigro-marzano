package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.mv_events.FinalConfigurationEvent;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.network.Settings;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.ConflictResolutionEvent;
import it.polimi.se2019.view.vc_events.VcMatchConfigurationEvent;

import java.util.*;

import static java.lang.Thread.sleep;

public class SetUpController extends Controller {
    private List<Integer> skulls = new ArrayList<>();
    private List<String> configs = new ArrayList<>();
    private List<Boolean> isFinalFrenzy = new ArrayList<>();
    private int counter = 0;
    private Random random;


    public SetUpController(Game model, Server server, int roomNumber) {
        super(model, server, roomNumber);
        startTimer(Settings.MATCH_SETUP_TIMER);
    }

    @Override
    public void update(VCEvent message)
    {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //this is the only controller registered on matchMaking thus it cannot receive unsupported events
            Log.severe("Received unsupported event " + message);
            throw new UnsupportedOperationException("SetUpController: " + e.getMessage(), e);
        }
    }

    @Override
    public void dispatch(VcMatchConfigurationEvent message) {
        skulls.add(message.getSkulls());
        configs.add(message.getConf());
        isFinalFrenzy.add(message.isFrenzy());
        counter++;
        if (counter==5) {
            endTimer();
        }
    }

    @Override
    protected void endTimer() {
        super.endTimer();
        model.send(new FinalConfigurationEvent("*", mostVoted(skulls), mostVoted(configs), mostVoted(isFinalFrenzy)));
    }

    private <T> T mostVoted (List<T> objects){
        Map<T, Integer> votes= new HashMap<>();
        int maximum = -1;
        List<T> mostVoted= new ArrayList<>();
        for (T o: objects)
            votes.put(o, Collections.frequency(objects, o));
        for (Map.Entry<T, Integer> e: votes.entrySet()){
            if (maximum == -1 || e.getValue()>maximum){
                mostVoted.clear();
                mostVoted.add(e.getKey());
                maximum = e.getValue();
            }
            else
                mostVoted.add(e.getKey());
        }
        return mostVoted.get(random.nextInt(mostVoted.size()));
    }
}
