package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * This event communicate to the user the possible effects they can use with the chosen card.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class PossibleEffectsEvent extends MVEvent {
    private String name;
    private HashMap<String, Integer> effects = new HashMap<>();
    private boolean isWeapon;

    public PossibleEffectsEvent(String destination, String name, boolean isWeapon) {
        super(destination);
        this.name = name;
        this.isWeapon = isWeapon;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }


    public Map<String, Integer> getEffects() {
        return new HashMap<>(effects);
    }

    public void addEffect(String effectName, int effectType){
        effects.put(effectName, effectType);
    }

    public String getName() {
        return name;
    }

    public boolean isWeapon() {
        return isWeapon;
    }
}
