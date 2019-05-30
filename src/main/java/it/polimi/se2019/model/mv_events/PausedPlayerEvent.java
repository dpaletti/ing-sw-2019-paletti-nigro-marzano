package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class PausedPlayerEvent extends MVEvent {
    String pausedPlayer;

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
