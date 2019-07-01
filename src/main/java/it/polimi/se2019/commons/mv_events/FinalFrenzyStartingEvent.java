package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

public class FinalFrenzyStartingEvent extends MVEvent { //notifies all users that the last skull was removed from the track and that final frenzy is starting

    public FinalFrenzyStartingEvent (String destination){
        super(destination);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
