package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.HashMap;
import java.util.Map;

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
