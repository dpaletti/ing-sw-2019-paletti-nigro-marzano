package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

public class UpdatePointsEvent extends MVEvent {
    private int points;
    private String username;

    public UpdatePointsEvent (String username, String destination, int points){
        super(destination);
        this.points = points;
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
