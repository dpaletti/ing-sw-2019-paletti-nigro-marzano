package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

/**
 * This event notifies the ticking of a timer.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class TimerEvent extends MVEvent {
    private int timeToGo;

    public TimerEvent(String destination, int timeToGo){
        super(destination);
        this.timeToGo = timeToGo;
    }

    public int getTimeToGo() {
        return timeToGo;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
