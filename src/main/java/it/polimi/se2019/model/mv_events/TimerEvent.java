package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

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
        dispatcher.update(this);
    }
}
