package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class ConnectionRefusedEvent extends MVEvent {
    private String cause;
    private String temporaryToken;

    public ConnectionRefusedEvent(String token, String temporaryToken, String cause){
        super(token);
        this.temporaryToken = temporaryToken;
        this.cause = cause;
    }

    public String getCause() {
        return cause;
    }

    public String getTemporaryToken() {
        return temporaryToken;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
