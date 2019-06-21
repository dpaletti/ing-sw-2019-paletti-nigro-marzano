package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.network.Settings;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.ConflictResolutionEvent;
import it.polimi.se2019.view.vc_events.VcMatchConfigurationEvent;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class SetUpController extends Controller {
    private List<Integer> skulls = new ArrayList<>();
    private List<String> configs = new ArrayList<>();
    private List<Boolean> isFinalFrenzy = new ArrayList<>();
    private int counter = 0;
    private Thread timer;


    public SetUpController(Game model, Server server, int roomNumber, List<Integer> skulls, List<String> configs, List<Boolean> isFinalFrenzy) {
        super(model, server, roomNumber);
        this.skulls = skulls;
        this.configs = configs;
        this.isFinalFrenzy = isFinalFrenzy;
        startTimer();
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
            timer.interrupt();
            closeSetUp();
        }
    }

    private void startTimer (){
        int period = Settings.MATCH_SETUP_TIMER/10;
        timer = new Thread(() ->{
            int time = 0;
            try {
                while(time < Settings.MATCH_SETUP_TIMER && !Thread.currentThread().isInterrupted()){
                    model.timerTick(Settings.MATCH_SETUP_TIMER - time);
                    time += period;
                    sleep(period);
                }
                closeSetUp();
            }catch (InterruptedException e){
                Log.severe("MatchMaking timer interrupted");
                Thread.currentThread().interrupt();
            }});
        timer.start();
    }

    private void closeSetUp(){
        //calculate set up
    }
}
