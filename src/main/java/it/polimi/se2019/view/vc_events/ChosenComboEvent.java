package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.model.Combo;
import it.polimi.se2019.utility.PartialCombo;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

import java.util.ArrayList;
import java.util.List;

// player sends combo they want to use

public class ChosenComboEvent extends VCEvent {
    private Combo chosenCombo;

    public ChosenComboEvent (String source, Combo chosenCombo) {
        super(source);
        this.chosenCombo = chosenCombo;
    }

    public Combo getChosenCombo() {
        return chosenCombo;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
