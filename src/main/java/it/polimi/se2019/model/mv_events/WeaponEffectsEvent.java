package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class WeaponEffectsEvent extends MVEvent {
    private ArrayList<String> weaponEffects; //names of available effects of the weapon
    public WeaponEffectsEvent (String destination, List<String> weaponEffects){
        super(destination);
        this.weaponEffects= new ArrayList<>(weaponEffects);
    }

    public List<String> getWeaponEffects() {
        return weaponEffects;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
