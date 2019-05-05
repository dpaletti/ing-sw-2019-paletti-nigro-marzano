package it.polimi.se2019.view;

import java.util.Set;

public class FigureToAttackEvent extends MVEvent {

    Set<String> playersToAttack;

    public Set<String> getPlayersToAttack() {
        return playersToAttack;
    }

    public void setPlayersToAttack(Set<String> playersToAttack) {
        this.playersToAttack = playersToAttack;
    }
}
