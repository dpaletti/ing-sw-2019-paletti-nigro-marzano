package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

/**
 * This event notifies the disconnection or reconnection of a user during the game.
 * See {@link it.polimi.se2019.client.view.VCEvent}.
 */

public class DisconnectionEvent extends VCEvent {
    private boolean isReconnection;
    public DisconnectionEvent(String token, boolean isReconnection){
        super(token);
        this.isReconnection = isReconnection;
    }


    public boolean isReconnection() {
        return isReconnection;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
