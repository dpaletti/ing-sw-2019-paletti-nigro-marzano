package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

public class DisconnectionEvent extends VCEvent {

    public DisconnectionEvent(String remoteEndId){
        super(remoteEndId);
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
