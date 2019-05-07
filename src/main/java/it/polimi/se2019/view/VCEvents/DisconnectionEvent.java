package it.polimi.se2019.view.VCEvents;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class DisconnectionEvent extends VCEvent {

    public DisconnectionEvent(String remoteEndId){
        super(remoteEndId);
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
