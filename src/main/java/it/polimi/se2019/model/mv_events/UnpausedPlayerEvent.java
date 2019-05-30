package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class UnpausedPlayerEvent extends MVEvent {
    private String unpausedPlayer;

    public UnpausedPlayerEvent(String destination, String unpausedPlayer){
        super(destination);
        this.unpausedPlayer= unpausedPlayer;
    }

    public String getUnpausedPlayer() {
        return unpausedPlayer;
    }

    @Override
    public void handle (MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}

