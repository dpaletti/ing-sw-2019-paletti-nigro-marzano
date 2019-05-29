package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class MvReconnectionEvent extends MVEvent {
    private String oldToken;
    private boolean matchMaking;

    public MvReconnectionEvent(String destination, String oldToken, boolean matchMaking){
        super(destination);
        this.oldToken = oldToken;
        this.matchMaking = matchMaking;
    }

    public String getOldToken() {
        return oldToken;
    }

    public boolean isMatchMaking() {
        return matchMaking;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
