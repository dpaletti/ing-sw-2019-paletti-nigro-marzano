package it.polimi.se2019.controller;

import com.sun.org.apache.bcel.internal.classfile.Unknown;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.network.Connection;
import it.polimi.se2019.view.DisconnectionEvent;
import it.polimi.se2019.view.JoinEvent;
import it.polimi.se2019.view.VirtualView;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvent;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


public class MatchController extends Controller{
    List<String> usernames;

    public MatchController(VirtualView virtualView, List<String> usernames){
        super(virtualView);
        if(usernames == null)
            throw new NullPointerException("Null usernames list not permitted");
        if(usernames.size() < 3)
            throw new InvalidParameterException("Cannot start a game with less than 3 people");
        usernames = new ArrayList<>(usernames);

        startMatch();
    }

    @Override
    public void update(VCEvent message) {
        try{
            message.handle(this);
        }catch (UnsupportedOperationException e){
            Log.severe(e.getMessage());
        }
    }

    public void update(DisconnectionEvent message){
        virtualView.timeOut(virtualView.getConnectionOnId(message.getUsername()));
        Log.info("Client " + message.getUsername() + " just disconnected");
        //TODO pause player in the model
    }

    public void update(JoinEvent message) {
        int reconnectionCounter = 0;
        Log.fine("Handling JoinEvent in MatchController");
        for (Connection c :
                virtualView.getTimeOuts()) {
            if (c.getUsername().equals(message.getUsername()))
                reconnectionCounter++;
        }
            if (reconnectionCounter == 2) {
                virtualView.reconnectPlayer(message);
                Log.info("Client" + message.getUsername() + "just reconnected");
                //TODO unpause the player
                //needs a map between player and connections
            }
            else{
                Log.info("Refusing connection from " + message.getUsername());
                //TODO file refusing connection event
            }
    }

    private void startMatch(){
        model = new Game();
        virtualView.register(this);
        for (Connection c:
            virtualView.getConnections()) {
            model.newPlayer();
        }
    }


}
