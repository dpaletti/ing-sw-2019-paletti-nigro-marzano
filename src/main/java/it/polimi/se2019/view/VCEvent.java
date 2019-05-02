package it.polimi.se2019.view;

import it.polimi.se2019.controller.MatchController;
import it.polimi.se2019.controller.MatchMakingController;
import it.polimi.se2019.network.NetworkHandlerRMI;
import it.polimi.se2019.network.NetworkHandlerSocket;

import java.net.InetAddress;

public class VCEvent extends Event {
    //Event coming from the view to the controller

    private InetAddress source;

    public VCEvent(InetAddress source){
        this.source = source;
    }

    public InetAddress getSource() {
        return source;
    }

    public void handle(MatchMakingController controller) {
        throw new UnsupportedOperationException("MatchMaking controller can't handle this event");
    }

    public void handle(MatchController controller){
        throw new UnsupportedOperationException("MatchController can't handle this event");
    }

    public void handle(NetworkHandlerSocket networkHandler) {
        throw new UnsupportedOperationException("Network Handler can't handle this event");
    }

    public void handle(NetworkHandlerRMI networkHandler) {
        throw new UnsupportedOperationException("Network Handler can't handle this event");
    }


}
