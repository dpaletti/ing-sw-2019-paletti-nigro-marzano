package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class DrawedPowerUpEvent extends MVEvent {
    private String drawed;
    private String user;

    public DrawedPowerUpEvent(String destination, String drawed,String user) {
        super(destination);
        this.drawed = drawed;
        this.user=user;
    }

    public String getUser() {
        return user;
    }

    public String getDrawed() {
        return drawed;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
