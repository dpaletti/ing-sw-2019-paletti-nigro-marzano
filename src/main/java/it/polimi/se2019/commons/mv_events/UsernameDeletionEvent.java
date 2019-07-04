package it.polimi.se2019.commons.mv_events;


import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

public class UsernameDeletionEvent extends MVEvent {
    private String username;
    private int missingPlayers;

    public UsernameDeletionEvent(String destination, String username, int missingPlayers){
        super(destination);
        this.username = username;
        this.missingPlayers = missingPlayers;
    }

    public int getMissingPlayers() {
        return missingPlayers;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
