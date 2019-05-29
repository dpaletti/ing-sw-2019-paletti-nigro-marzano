package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class TurnEvent extends MVEvent {
    private ArrayList<String> playingOrder;

    public TurnEvent(String destination, List<String> playingOrder){
        super(destination);
        this.playingOrder= new ArrayList<>(playingOrder);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
