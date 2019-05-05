package it.polimi.se2019.view;

import java.util.Set;

public class EffectToApplyEvent extends MVEvent {
    Set<String> applicableEffects;

    public Set<String> getApplicableEffects() {
        return applicableEffects;
    }

    public void setApplicableEffects(Set<String> applicableEffects) {
        this.applicableEffects = applicableEffects;
    }
}
