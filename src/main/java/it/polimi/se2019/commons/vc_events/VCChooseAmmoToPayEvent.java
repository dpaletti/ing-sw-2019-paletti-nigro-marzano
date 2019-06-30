package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

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
