package it.polimi.se2019.model;

import java.util.List;

public class TurnMemory {
    private Turn turn;
    private List<String> usedEffects;
    private List<List<Tile>> shotTiles;
    private List<List<Figure>> shotFigures;

    public void turnMemory (Turn turn, List<String> usedEffects, List<List<Tile>> shotTiles, List<List<Figure>> shotFigures){
        this.turn=turn;
        this.shotFigures=shotFigures;
        this.shotTiles=shotTiles;
    }
    public Turn getTurn() {
        return turn;
    }

    public List<List<Figure>> getShotFigures() {
        return shotFigures;
    }

    public List<List<Tile>> getShotTiles() {
        return shotTiles;
    }

    public List<String> getUsedEffects() {
        return usedEffects;
    }

    public List<Figure> mapEffectToTargets (String effect){
        int indexOfEffect= usedEffects.indexOf(effect);
        return (shotFigures.get(indexOfEffect));
    }

    public List<Tile> mapEffectToTiles (String effect){
        int indexOfEffect= usedEffects.indexOf(effect);
        return (shotTiles.get(indexOfEffect));
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public void setShotFigures(List<List<Figure>> shotFigures) {
        this.shotFigures = shotFigures;
    }

    public void setShotTiles(List<List<Tile>> shotTiles) {
        this.shotTiles = shotTiles;
    }

    public void setUsedEffects(List<String> usedEffects) {
        this.usedEffects = usedEffects;
    }


    public void clear (){
        turn.setFirstCombo(null);
        turn.setSecondCombo(null);
        turn.setFirstTargetSet(null);
        turn.setSecondTargetSet(null);
        usedEffects.clear();
        shotTiles.clear();
        shotFigures.clear();
    }
}
