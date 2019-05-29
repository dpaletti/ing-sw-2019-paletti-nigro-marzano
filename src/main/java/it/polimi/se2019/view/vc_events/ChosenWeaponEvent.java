package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class ChosenWeaponEvent extends VCEvent {
    private String weapon;

    public ChosenWeaponEvent (String source, String weapon){
        super(source);
        this.weapon=weapon;
    }
    public String getWeapon() {
        return weapon;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
