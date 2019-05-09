package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.model.FigureColour;
import it.polimi.se2019.view.MVEvent;

import java.util.Set;

public class FigureToAttackEvent extends MVEvent {

    private Set<FigureColour> playersToAttack;

    public FigureToAttackEvent(){
        super();
    }

    public FigureToAttackEvent(String destination){
        super(destination);
    }

    public Set<FigureColour> getPlayersToAttack() {
        return playersToAttack;
    }

    public void setPlayersToAttack(Set<FigureColour> playersToAttack) {
        this.playersToAttack = playersToAttack;
    }
}
