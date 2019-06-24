package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.PartialCombo;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

import java.util.ArrayList;
import java.util.List;

// player sends list of partial combo they want to use (a list corresponds to one of the possible combos eg grab stuff, shoot people...

public class ChosenComboEvent extends VCEvent {
    private ArrayList<PartialCombo> chosenCombo;

    public ChosenComboEvent (String source, List<PartialCombo> chosenCombo) {
        super(source);
        this.chosenCombo = new ArrayList<>(chosenCombo);
    }

    public ArrayList<PartialCombo> getChosenCombo() {
        return chosenCombo;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
