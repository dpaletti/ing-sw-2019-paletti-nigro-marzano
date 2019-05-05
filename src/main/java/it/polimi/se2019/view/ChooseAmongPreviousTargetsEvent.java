package it.polimi.se2019.view;

import java.util.Set;

public class ChooseAmongPreviousTargetsEvent extends MVEvent {
    private Set<String> previousTargets;

    public Set<String> getPreviousTargets() {
        return previousTargets;
    }

    public void setPreviousTargets(Set<String> previousTargets) {
        this.previousTargets = previousTargets;
    }
}
