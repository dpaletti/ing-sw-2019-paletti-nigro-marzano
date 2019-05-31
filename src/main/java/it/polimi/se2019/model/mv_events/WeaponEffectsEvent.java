package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class WeaponEffectsEvent extends MVEvent {
    private ArrayList<String> weaponEffectsPositions; //position of box on the card

    public WeaponEffectsEvent (String destination, List<String> weaponEffectsPositions){
        super(destination);
        this.weaponEffectsPositions= new ArrayList<>(weaponEffectsPositions);
    }

    public List<String> getWeaponEffectsPositions() {
        return weaponEffectsPositions;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
