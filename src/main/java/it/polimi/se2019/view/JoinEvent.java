package it.polimi.se2019.view;

import it.polimi.se2019.controller.MatchMakingController;

import java.net.InetAddress;

public class JoinEvent extends VCEvent {

    public JoinEvent(InetAddress ip){
        super(ip);
    }

    public JoinEvent(){
        super();
    }
    @Override
    public void handle(MatchMakingController controller) {
        controller.update(this);
    }
}
