package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.client.view.MVEvent;
import it.polimi.se2019.commons.utility.MVEventDispatcher;

/**
 * This event is sent when a user discards a power up and everyone is notified.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class MVDiscardPowerUpEvent extends MVEvent {
    private String discardedPowerUp;
    private String username;

    public MVDiscardPowerUpEvent(String destination, String discardedPowerUp, String username) {
        super(destination);
        this.discardedPowerUp = discardedPowerUp;
        this.username = username;
    }

    public String getDiscardedPowerUp() {
        return discardedPowerUp;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
