package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class DrawnPowerUpEvent extends MVEvent {
    private String drawn;

    public DrawnPowerUpEvent(String destination, String drawn) {
        super(destination);
        this.drawn = drawn;
    }

    public String getDrawn() {
        return drawn;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
