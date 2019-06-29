package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.TimerEvent;
import it.polimi.se2019.utility.Log;

import static java.lang.Thread.sleep;

public class TickingTimer {
    private Thread timer;
    private Game model;
    private Runnable endOperation;

    public TickingTimer(Game model, Runnable endOperation){
        this.model = model;
        this.endOperation = endOperation;
    }

    public void startTimer (int duration){
        timer = new Thread(() ->{
            int time = 0;
            try {
                while(time < duration && !Thread.currentThread().isInterrupted()){
                    model.send(new TimerEvent("*", duration - time));
                    time += 1000;
                    sleep(1000);
                }
                endTimer();
            }catch (InterruptedException e){
                Log.severe("Timer interrupted");
                Thread.currentThread().interrupt();
            }});
        timer.start();
    }


    public void endTimer(){
        if(timer == null)
            return;
        if (!timer.isInterrupted())
            timer.interrupt();
        endOperation.run();
    }

}
