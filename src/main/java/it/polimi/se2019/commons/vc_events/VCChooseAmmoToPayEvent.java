package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

/**
 * This event is sent when a user decides which ammo to use to pay when using a power up that requires a payment.
 * See {@link it.polimi.se2019.client.view.VCEvent}.
 */

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
