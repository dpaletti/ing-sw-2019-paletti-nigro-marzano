package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class DiscardedPowerUpEvent extends MVEvent {
    String playing;
    String drawnPowerUp;
    String discardedPowerUp;

    public DiscardedPowerUpEvent (String destination, String playing, String drawnPowerUp, String discardedPowerUp){
        super(destination);
        this.playing= playing;
        this.discardedPowerUp= discardedPowerUp;
        this.drawnPowerUp= drawnPowerUp;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
