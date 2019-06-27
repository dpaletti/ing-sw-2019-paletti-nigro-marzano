package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.HashMap;
import java.util.Map;

public class PossiblePowerUpEffectsEvent extends MVEvent {
    private String powerUp;
    private HashMap<String, Integer> effects = new HashMap<>();

    public PossiblePowerUpEffectsEvent(String destination, String powerUp){
        super (destination);
        this.powerUp = powerUp;
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

    public String getPowerUp() {
        return powerUp;
    }
}
