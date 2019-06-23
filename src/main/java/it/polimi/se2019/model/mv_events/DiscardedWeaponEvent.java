package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class DiscardedWeaponEvent extends MVEvent {
    private String playing;
    private String drawnWeapon;
    private String discardedWeapon;

    public DiscardedWeaponEvent(String destination, String playing,String discardedWeapon,String drawnWeapon) {
        super(destination);
        this.playing = playing;
        this.discardedWeapon=discardedWeapon;
        this.drawnWeapon=drawnWeapon;
    }

    public String getPlaying() {
        return playing;
    }

    public String getDiscardedWeapon() {
        return discardedWeapon;
    }

    public String getDrawnWeapon() {
        return drawnWeapon;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
