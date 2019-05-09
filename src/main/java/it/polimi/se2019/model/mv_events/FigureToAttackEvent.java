package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.view.MVEvent;

import java.util.Set;

public class FigureToAttackEvent extends MVEvent {

    private Set<String> playersToAttack;

    public FigureToAttackEvent(){
        super();
    }

    public FigureToAttackEvent(String destination){
        super(destination);
    }

    public Set<String> getPlayersToAttack() {
        return playersToAttack;
    }

    public void setPlayersToAttack(Set<String> playersToAttack) {
        this.playersToAttack = playersToAttack;
    }
}
