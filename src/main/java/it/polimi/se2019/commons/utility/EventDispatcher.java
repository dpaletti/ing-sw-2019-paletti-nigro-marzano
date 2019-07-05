package it.polimi.se2019.commons.utility;

import it.polimi.se2019.commons.utility.Event;

/**
 * This interface dispatches events arriving both from the View or the Model, it is extended by {@link it.polimi.se2019.commons.utility.EventDispatcher}.
 */

public interface EventDispatcher {
    default void dispatch(Event event){
        throw new UnsupportedOperationException("Cannot handle generic event");
    }
}
