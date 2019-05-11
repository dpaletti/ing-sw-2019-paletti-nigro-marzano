package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.Set;

public class EffectToApplyEvent extends MVEvent {
    Set<String> applicableEffects;

    public EffectToApplyEvent(){
        super();
    }

    public EffectToApplyEvent(String destination){
        super(destination);
    }

    public Set<String> getApplicableEffects() {
        return applicableEffects;
    }

    public void setApplicableEffects(Set<String> applicableEffects) {
        this.applicableEffects = applicableEffects;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
