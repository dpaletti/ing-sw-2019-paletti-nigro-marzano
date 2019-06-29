package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class MVRespawnEvent extends MVEvent {
    private String drawnPowerUpName;

    public MVRespawnEvent(String destination, String drawnPowerUpName) {
        super(destination);
        this.drawnPowerUpName = drawnPowerUpName;
    }

    public String getDrawnPowerUpName() {
        return drawnPowerUpName;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
