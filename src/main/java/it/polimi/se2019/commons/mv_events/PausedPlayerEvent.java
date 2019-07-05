package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

/**
 * This event notifies all users whenever a player is paused.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class PausedPlayerEvent extends MVEvent {
    private String pausedPlayer;

    public PausedPlayerEvent (String destination, String pausedPlayer){
        super(destination);
        this.pausedPlayer= pausedPlayer;
    }

    public String getPausedPlayer() {
        return pausedPlayer;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
