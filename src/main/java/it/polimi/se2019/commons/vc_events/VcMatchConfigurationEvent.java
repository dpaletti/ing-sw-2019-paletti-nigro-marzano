package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

public class VcMatchConfigurationEvent extends VCEvent {
    private int skulls;
    private boolean frenzy;
    private String conf;

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public VcMatchConfigurationEvent(String source, int skulls, boolean frenzy, String conf) {
        super(source);
        this.skulls = skulls;
        this.frenzy = frenzy;
        this.conf = conf;
    }

    public int getSkulls() {
        return skulls;
    }

    public String getConf() {
        return conf;
    }

    public boolean isFrenzy() {
        return frenzy;
    }
}
