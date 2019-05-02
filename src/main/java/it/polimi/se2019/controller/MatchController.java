package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.network.Connection;
import it.polimi.se2019.view.DisconnectionEvent;
import it.polimi.se2019.view.JoinEvent;
import it.polimi.se2019.view.VirtualView;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvent;

public class MatchController extends Controller{

    public MatchController(VirtualView virtualView){
        super(virtualView);
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
        virtualView.timeOut(message.getSource());
        for (Player p:
             model.getPlayers()) {
            if(p.getIp().equals(message.getSource())){
                p.pause();
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    public void update(JoinEvent message){
        virtualView.kick(message.getSource());
    }

    private void startMatch(){
        model = new Game();
        virtualView.register(this);
        for (Connection c:
            virtualView.getConnectionList()) {
            model.newPlayer(c.getIp());
        }
    }


}
