package it.polimi.se2019.server.controller;

import it.polimi.se2019.server.model.AmmoColour;
import it.polimi.se2019.server.model.Game;
import it.polimi.se2019.server.model.Player;
import it.polimi.se2019.server.model.PowerUp;
import it.polimi.se2019.commons.mv_events.DisablePowerUpEvent;
import it.polimi.se2019.server.network.Server;
import it.polimi.se2019.commons.utility.Observer;
import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

/**
 * This class is extended by all the controllers used in the Server. It implements {@link it.polimi.se2019.commons.utility.Observer}
 * in order to receive and dispatch correctly the events arriving from the view.
 */

public abstract class Controller implements Observer<VCEvent>, VCEventDispatcher {
    protected Game model;
    protected Server server;
    private int roomNumber;
    protected boolean disabled = false;

    public Controller(Game model, Server server, int roomNumber){
        this.model = model;
        this.server = server;
        this.roomNumber = roomNumber;
        server.addController(this,roomNumber);
    }

    public Controller (){
        //empty constructor
    }

    /**
     * disables a controller without deleting it in order to avoid concurrent modification exceptions.
     */
    public void disable(){
        disabled = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    /**
     * calculates whether a match has enough active players.
     * @return if the match has enough active players to keep running, it returns true.
     */
    public boolean enoughActivePlayers (){
        int active = 0;
        for (Player p : model.getPlayers()) {
            if(!p.isPaused())
                active++;
        }
        return !(active < 3);
    }

    /**
     * calculates the next active user in the game.
     * @param user the currently playing user.
     * @return the next active user in the turn.
     */
    public String getNextActiveUser (String user){
        if (model.getUsernames().indexOf(user) + 1 >= model.getUsernames().size())
            return model.getUsernames().get(0);

        if (model.userToPlayer(model.getUsernames().get(model.getUsernames().indexOf(user) + 1)).isPaused())
            return getNextActiveUser(model.getUsernames().get(model.getUsernames().indexOf(user) + 1));
        return model.getUsernames().get(model.getUsernames().indexOf(user) + 1);
    }

    /**
     * checks whether power ups that cannot be used are present and, in case they are, it notifies the view in order to deactivate them
     * @param currentPlayer the player playing.
     * @param constraint the constraint of the power up (eg: during the turn, while being attacked, ...).
     */

    protected void disablePowerUps(String currentPlayer, String constraint){
        for (PowerUp p : model.userToPlayer(currentPlayer).getPowerUps()) {
            if (p.getConstraint().equalsIgnoreCase(constraint))
                model.send(new DisablePowerUpEvent(currentPlayer, p.getName()));
        }
    }

    /**
     * generates an ammo colour from the name string.
     * @param ammoName the name of the colour of the ammo.
     * @return the ammo colour.
     */
    protected AmmoColour stringToAmmo (String ammoName){
        for (AmmoColour ammoColour: AmmoColour.values()){
            if (ammoColour.toString().equalsIgnoreCase(ammoName)){
                return ammoColour;
            }
        }
        throw new NullPointerException("This ammo doesn't exist: " + ammoName);
    }
}
