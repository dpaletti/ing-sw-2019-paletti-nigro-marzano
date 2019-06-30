package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

import java.util.ArrayList;
import java.util.List;

public class VCSellPowerUpEvent extends VCEvent {
    private ArrayList<String> powerUpsToSell;

    public VCSellPowerUpEvent(String source, List<String> powerUpsToSell) {
        super(source);
        this.powerUpsToSell = new ArrayList<>(powerUpsToSell);
    }

    public List<String> getPowerUpsToSell() {
        return powerUpsToSell;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
