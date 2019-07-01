package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

// player sends combo they want to use

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
