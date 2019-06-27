package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class ChosenEffectPowerUpEvent extends VCEvent {
    private String effectName;
    private String powerUp;

    public ChosenEffectPowerUpEvent (String source, String effectName, String powerUp){
        super(source);
        this.effectName= effectName;
        this.powerUp= powerUp;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getEffectName() {
        return effectName;
    }

    public String getPowerUp() {
        return powerUp;
    }
}
