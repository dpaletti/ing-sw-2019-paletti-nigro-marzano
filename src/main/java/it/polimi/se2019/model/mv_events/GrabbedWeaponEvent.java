package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class GrabbedWeaponEvent extends MVEvent {
    private String weapon;
    private String user;

    public GrabbedWeaponEvent(String destination, String weapon, String user) {
        super(destination);
        this.user=user;
        this.weapon=weapon;
    }

    public String getUser() {
        return user;
    }

    public String getWeapon() {
        return weapon;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
