package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

/**
 * This event is sent when the connection is refused.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class ConnectionRefusedEvent extends MVEvent {
    private String cause;

    public ConnectionRefusedEvent(String token, String cause){
        super(token);
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
