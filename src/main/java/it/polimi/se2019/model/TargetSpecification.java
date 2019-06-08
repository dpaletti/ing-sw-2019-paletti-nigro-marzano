package it.polimi.se2019.model;

import it.polimi.se2019.utility.Pair;

import java.util.List;

public class TargetSpecification  {
    private boolean tile;
    private Pair<Boolean, List<String>> different;
    private Pair<Boolean, List<String>> previous;
    private Pair<Integer, Integer> radiusBetween;
    private int visible;
    private int enlarge;

    public TargetSpecification(TargetSpecification targetSpecification){
        tile= targetSpecification.getTile();
        different= targetSpecification.getDifferent();
        previous= targetSpecification.getPrevious();
        radiusBetween= targetSpecification.getRadiusBetween();
        visible= targetSpecification.getVisible();
        enlarge= targetSpecification.getEnlarge();
    }



    public boolean getTile() { return tile; }

    public int getEnlarge() { return enlarge; }

    public int getVisible() { return visible; }

    public Pair<Integer, Integer> getRadiusBetween() {
        return new Pair<>(radiusBetween.getFirst(), radiusBetween.getSecond());
    }

    public Pair<Boolean, List<String>> getDifferent() {
        return new Pair<>(different.getFirst(), different.getSecond());
    }

    public Pair<Boolean, List<String>> getPrevious() {
        return new Pair<>(previous.getFirst(), previous.getSecond());
    }

}

