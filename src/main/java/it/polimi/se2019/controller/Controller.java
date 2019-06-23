package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

import static java.lang.Thread.sleep;

public abstract class Controller implements Observer<VCEvent>, VCEventDispatcher {
    //TODO evaluate the need for storing a reference to the model, probably needed
    protected Game model;
    protected Server server;
    private int roomNumber;
    private Thread timer;

    public Controller(Game model, Server server, int roomNumber){
        this.model = model;
        this.server = server;
        this.roomNumber = roomNumber;
        server.addController(this,roomNumber);
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    protected void startTimer (int duration){
        timer = new Thread(() ->{
            int time = 0;
            try {
                while(time < duration && !Thread.currentThread().isInterrupted()){
                    model.timerTick(duration - time);
                    time += 1000;
                    sleep(1000);
                }
                endTimer();
            }catch (InterruptedException e){
                Log.severe("MatchMaking timer interrupted");
                Thread.currentThread().interrupt();
            }});
        timer.start();
    }

    protected void endTimer(){
        if (!timer.isInterrupted())
            timer.interrupt();
    }
}
