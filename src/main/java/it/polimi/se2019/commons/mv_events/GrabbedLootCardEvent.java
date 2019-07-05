package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

/**
 * This event notifies all users when a loot card is grabbed.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class GrabbedLootCardEvent extends MVEvent {
    private String grabbedLootCard;

    public GrabbedLootCardEvent(String destination, String grabbedLootCard) {
        super(destination);
        this.grabbedLootCard = grabbedLootCard;
    }

    public String getGrabbedLootCard() {
        return grabbedLootCard;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
