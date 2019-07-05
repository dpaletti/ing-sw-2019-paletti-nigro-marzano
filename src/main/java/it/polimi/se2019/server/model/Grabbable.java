package it.polimi.se2019.server.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This interface describes the behaviour of weapons and loot cards, which are both elements that can be grabbed on the map.
 */

public interface Grabbable {

    String getName();

    void grab(Player playing, String grabbed, Game game);

    static List<String> grabbableStringify(List<Grabbable> grabbables){
        List<String> names = new ArrayList<>();
        for (Grabbable g : grabbables)
            names.add(g.getName());
        return names;
    }

    static List<Grabbable> grabbableToCard(List<? extends Grabbable> grabbables){
        List<Grabbable> toReturn = new ArrayList<>();
        Iterator<? extends Grabbable> iterator=grabbables.iterator();
        while (iterator.hasNext()){
            toReturn.add(iterator.next());
        }
        return toReturn;
    }


}
