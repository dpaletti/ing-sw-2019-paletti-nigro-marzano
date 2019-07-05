package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This event communicates that the user wishes to reload the listed weapons.
 * See {@link it.polimi.se2019.client.view.VCEvent}.
 */

public class ReloadEvent extends VCEvent {
    private ArrayList<String> reloadedWeapons;

    public ReloadEvent (String source, List<String> weaponName){
        super(source);
        this.reloadedWeapons = new ArrayList<>(weaponName);
    }

    public ArrayList<String> getReloadedWeapons() {
        return reloadedWeapons;
    }

    public void setReloadedWeapons(ArrayList<String> reloadedWeapons) {
        this.reloadedWeapons = reloadedWeapons;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
