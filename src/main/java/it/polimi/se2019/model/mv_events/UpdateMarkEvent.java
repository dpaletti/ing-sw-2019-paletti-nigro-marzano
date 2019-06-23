package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class UpdateMarkEvent extends MVEvent {
    private String marked;
    private String marker;

    public UpdateMarkEvent (String destination, String marked, String marker){
        super(destination);
        this.marked= marked;
        this.marker= marker;
    }

    public String getMarked() {
        return marked;
    }

    public String getMarker() {
        return marker;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}