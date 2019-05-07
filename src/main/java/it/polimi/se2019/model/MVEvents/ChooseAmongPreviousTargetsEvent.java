package it.polimi.se2019.model.MVEvents;

import it.polimi.se2019.view.MVEvent;

import java.util.Set;

public class ChooseAmongPreviousTargetsEvent extends MVEvent {
    private Set<String> previousTargets;

    public ChooseAmongPreviousTargetsEvent(){
        super();
    }

    public ChooseAmongPreviousTargetsEvent(String destination){
        super(destination);
    }

    public Set<String> getPreviousTargets() {
        return previousTargets;
    }

    public void setPreviousTargets(Set<String> previousTargets) {
        this.previousTargets = previousTargets;
    }
}
