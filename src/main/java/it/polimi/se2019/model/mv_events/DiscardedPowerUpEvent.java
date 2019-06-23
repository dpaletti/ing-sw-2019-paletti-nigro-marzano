package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class DiscardedPowerUpEvent extends MVEvent {
    private String playing;
    private String drawnPowerUp;        // if "none" no powerUp was drawn
    private String discardedPowerUp;

    public DiscardedPowerUpEvent (String destination, String playing, String drawnPowerUp, String discardedPowerUp){
        super(destination);
        this.playing= playing;
        this.discardedPowerUp= discardedPowerUp;
        this.drawnPowerUp= drawnPowerUp;
    }

    public String getDiscardedPowerUp() {
        return discardedPowerUp;
    }

    public String getDrawnPowerUp() {
        return drawnPowerUp;
    }

    public String getPlaying() {
        return playing;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
