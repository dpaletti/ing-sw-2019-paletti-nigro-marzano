package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class FinalConfigurationEvent extends MVEvent {
    private int skulls;
    private String config;
    private boolean frenzy;

    public FinalConfigurationEvent(String destination, int skulls, String config, boolean frenzy) {
        super(destination);
        this.skulls = skulls;
        this.config = config;
        this.frenzy = frenzy;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getConfig() {
        return config;
    }

    public int getSkulls() {
        return skulls;
    }

    public boolean isFrenzy() {
        return frenzy;
    }
}
