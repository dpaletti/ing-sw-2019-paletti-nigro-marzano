package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class VCChooseAmmoToPayEvent extends VCEvent {
    private String chosenAmmo;

    public VCChooseAmmoToPayEvent(String source, String chosenAmmo) {
        super(source);
        this.chosenAmmo = chosenAmmo;
    }

    public String getChosenAmmo() {
        return chosenAmmo;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
