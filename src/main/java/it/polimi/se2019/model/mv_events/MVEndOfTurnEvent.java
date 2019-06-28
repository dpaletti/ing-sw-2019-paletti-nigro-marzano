package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class MVEndOfTurnEvent extends MVEvent {
    private String playerEnding;
    private String playerStarting;

    public MVEndOfTurnEvent(String destination, String playerEnding, String playerStarting) {
        super(destination);
        this.playerEnding = playerEnding;
        this.playerStarting = playerStarting;
    }

    public String getPlayerEnding() {
        return playerEnding;
    }

    public String getPlayerStarting() {
        return playerStarting;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
