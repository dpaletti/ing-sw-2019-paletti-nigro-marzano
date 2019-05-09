package it.polimi.se2019.model.mv_events;


import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class UsernameDeletionEvent extends MVEvent {

    public UsernameDeletionEvent(String username){
        super(username);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
