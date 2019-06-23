package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class UpdateMarkEvent extends MVEvent {
    private String marked;
    private String marker;
    private boolean adding;

    public UpdateMarkEvent (String destination, String marked, String marker, boolean adding){
        super(destination);
        this.marked = marked;
        this.marker = marker;
        this.adding = adding;
    }

    public String getMarked() {
        return marked;
    }

    public String getMarker() {
        return marker;
    }

    public boolean isAdding() {
        return adding;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}