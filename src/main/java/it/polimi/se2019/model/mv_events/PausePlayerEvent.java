package it.polimi.se2019.model.mv_events;


import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class PausePlayerEvent extends MVEvent {

    public PausePlayerEvent(String destination){
        super(destination);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
