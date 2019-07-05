package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.utility.Pair;

import java.util.List;

/**
 * This class defines the properties targets need to have in order to be hit by each weapon.
 * The tile property is true when a weapon hits all the targets on a specific tile.
 * The different property is true when a weapon can only hit targets that where not hit in the defined effects.
 * The previous property is true when a weapon can only hit targets that where hit in the defined effects.
 * The radius between property defines the minimum and maximum distance the weapon can hit.
 * Some specific pairs of values have a different meaning: (0,0) means that the target is the player using the weapon,
 * (-2, -2) means that the targets are all the ones in a visible area, (-3, -3) means that the targets are all those in a different room,
 * -1 means that the radius can be of any value. When the two values are the same, the targets have to be at that exact distance.
 * The visible property is -1 when the visibility property is not considered, 0 when the targets are non-visible players, 1 when they are visible and 2 when they are visible to the previous target.
 */

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

