package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class EndOfTurnEvent extends VCEvent {

    public EndOfTurnEvent (String source){
        super(source);
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
