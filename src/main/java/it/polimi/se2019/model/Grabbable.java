package it.polimi.se2019.model;

import java.util.ArrayList;
import java.util.List;

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
        while (grabbables.iterator().hasNext()){
            toReturn.add(grabbables.iterator().next());
        }
        return toReturn;
    }


}
