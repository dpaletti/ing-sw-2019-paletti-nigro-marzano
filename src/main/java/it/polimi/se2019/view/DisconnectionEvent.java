package it.polimi.se2019.view;

import it.polimi.se2019.controller.MatchController;
import it.polimi.se2019.controller.MatchMakingController;

import java.net.InetAddress;

public class DisconnectionEvent extends VCEvent{

    public DisconnectionEvent(String remoteEndId){
        super(remoteEndId);
    }

    @Override
    public void handle(MatchMakingController controller) {
        controller.update(this);
    }

    @Override
    public void handle(MatchController controller) {
        controller.update(this);
    }
}
