package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

/**
 * This event is sent to all users to notify that a user drew a power up. The power ups of the other players are secret
 * and cannot be shown.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

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
