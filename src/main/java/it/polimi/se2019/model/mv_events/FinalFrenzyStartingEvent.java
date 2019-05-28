package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class FinalFrenzyStartingEvent extends MVEvent { //notifies all users that the last skull was removed from the track and that final frenzy is starting

    public FinalFrenzyStartingEvent (String destination){
        super(destination);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
