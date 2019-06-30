package it.polimi.se2019.controller;

import it.polimi.se2019.model.AmmoColour;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PowerUp;
import it.polimi.se2019.model.mv_events.DisablePowerUpEvent;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public abstract class Controller implements Observer<VCEvent>, VCEventDispatcher {
    //TODO evaluate the need for storing a reference to the model, probably needed
    protected Game model;
    protected Server server;
    private int roomNumber;

    public Controller(Game model, Server server, int roomNumber){
        this.model = model;
        this.server = server;
        this.roomNumber = roomNumber;
        server.addController(this,roomNumber);
    }

    public Controller (){
        //empty constructor
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean enoughActivePlayers (){
        int active = 0;
        for (Player p : model.getPlayers()) {
            if(!p.isPaused())
                active++;
        }
        return !(active < 3);
    }

    public String getNextActiveUser (String user){
        if (model.userToPlayer(model.getUsernames().get(model.getUsernames().indexOf(user) + 1)).isPaused())
            return getNextActiveUser(model.getUsernames().get(model.getUsernames().indexOf(user) + 1));
        else
            return model.getUsernames().get(model.getUsernames().indexOf(user) + 1);
    }

    protected void disablePowerUps(String currentPlayer, String constraint){
        for (PowerUp p : model.userToPlayer(currentPlayer).getPowerUps()) {
            if (p.getConstraint().equalsIgnoreCase(constraint))
                model.send(new DisablePowerUpEvent(currentPlayer, p.getName()));
        }
    }

    protected AmmoColour stringToAmmo (String ammoName){
        for (AmmoColour ammoColour: AmmoColour.values()){
            if (ammoColour.toString().equalsIgnoreCase(ammoName)){
                return ammoColour;
            }
        }
        throw new NullPointerException("This ammo doesn't exist: " + ammoName);
    }

}
