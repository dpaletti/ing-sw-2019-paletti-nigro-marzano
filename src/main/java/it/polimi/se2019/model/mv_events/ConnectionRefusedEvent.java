package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

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
        dispatcher.update(this);
    }
}
