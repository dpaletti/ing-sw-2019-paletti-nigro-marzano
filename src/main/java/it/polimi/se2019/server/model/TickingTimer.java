package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.mv_events.TimerEvent;
import it.polimi.se2019.commons.utility.Log;

import static java.lang.Thread.sleep;

/**
 * This class implements a timer whose duration can be set.
 * The timer uses the model to send an event to the view whenever the timer ticks and defines a runnable to execute whenever the timer is interrupted.
 *
 */

public class TickingTimer {
    private Thread timer;
    private Game model;
    private Runnable endOperation;

    public TickingTimer(Game model, Runnable endOperation){
        this.model = model;
        this.endOperation = endOperation;
    }

    /**
     * this method starts the timer and sends a notification to the view every millisecond.
     * @param duration
     */
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


    /**
     * this method is used when the timer ends and a endOperation runnable starts running. The runnable can be defined in the constructor.
     */
    public void endTimer(){
        if(timer == null) {
            endOperation.run();
            return;
        }
        if (!timer.isInterrupted())
            timer.interrupt();
        endOperation.run();
    }

    public void stop(){
        if (timer!=null && !timer.isInterrupted())
            timer.interrupt();

    }

}
