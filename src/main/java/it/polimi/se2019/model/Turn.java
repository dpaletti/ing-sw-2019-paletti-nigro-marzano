package it.polimi.se2019.model;

import java.util.List;
import java.util.Set;

public class Turn {
    private Combo firstCombo;
    private Set<Figure> firstTargetSet;
    private Combo secondCombo;
    private Set<Figure> secondTargetSet;
    private List<PowerUp> usedPowerUp;
    private List<String> usedEffects;
    private List<List<Tile>> shotTiles;
    private List<List<Figure>> shotFigures;

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

    public List<String> getUsedEffects() {
        return usedEffects;
    }

    public List<List<Tile>> getShotTiles() {
        return shotTiles;
    }

    public List<List<Figure>> getShotFigures() {
        return shotFigures;
    }

    public void setFirstCombo(Combo firstCombo) {
        this.firstCombo = firstCombo;
    }

    public void setFirstTargetSet(Set<Figure> firstTargetSet) {
        this.firstTargetSet = firstTargetSet;
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

    public List<Figure> mapEffectToTargets (String effect){
        int indexOfEffect= usedEffects.indexOf(effect);
        return (shotFigures.get(indexOfEffect));
    }

    public List<Tile> mapEffectToTiles (String effect){
        int indexOfEffect= usedEffects.indexOf(effect);
        return (shotTiles.get(indexOfEffect));
    }
}
