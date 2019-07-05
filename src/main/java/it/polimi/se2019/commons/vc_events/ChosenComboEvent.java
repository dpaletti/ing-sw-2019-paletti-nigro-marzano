package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

/**
 * This event is sent from the user in order to communicate the combo (eg: Run Around, Shoot People, Grab Stuff,...) they wish to use.
 * See {@link it.polimi.se2019.client.view.VCEvent}.
 */

public class ChosenComboEvent extends VCEvent {
    private String chosenCombo;

    public ChosenComboEvent (String source, String chosenCombo) {
        super(source);
        this.chosenCombo = chosenCombo;
    }

    public String getChosenCombo() {
        return chosenCombo;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
