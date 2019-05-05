package it.polimi.se2019.model;

import java.util.List;
import java.util.Set;

public class Turn {
    private Player player;
    private Combo firstCombo;
    private Set<Figure> firstTargetSet;
    private Combo secondCombo;
    private Set<Figure> secondTargetSet;
    private List<PowerUp> usedPowerUp;

    public Combo getFirstCombo() {
        return firstCombo;
    }

    public Set<Figure> getFirstTargetSet() {
        return firstTargetSet;
    }

    public Combo getSecondCombo() {
        return secondCombo;
    }

    public Set<Figure> getSecondTargetSet() {
        return secondTargetSet;
    }

    public List<PowerUp> getUsedPowerUp() {
        return usedPowerUp;
    }

    public Player getPlayer() {
        return player;
    }

    public void setFirstCombo(Combo firstCombo) {
        this.firstCombo = firstCombo;
    }

    public void setFirstTargetSet(Set<Figure> firstTargetSet) {
        this.firstTargetSet = firstTargetSet;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setSecondCombo(Combo secondCombo) {
        this.secondCombo = secondCombo;
    }

    public void setSecondTargetSet(Set<Figure> secondTargetSet) {
        this.secondTargetSet = secondTargetSet;
    }

    public void setUsedPowerUp(List<PowerUp> usedPowerUp) {
        this.usedPowerUp = usedPowerUp;
    }
}
