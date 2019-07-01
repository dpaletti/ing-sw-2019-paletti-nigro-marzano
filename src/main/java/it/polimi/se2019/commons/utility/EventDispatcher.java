package it.polimi.se2019.commons.utility;

import it.polimi.se2019.commons.utility.Event;

public interface EventDispatcher {
    default void dispatch(Event event){
        throw new UnsupportedOperationException("Cannot handle generic event");
    }
}
