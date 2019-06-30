package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

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
