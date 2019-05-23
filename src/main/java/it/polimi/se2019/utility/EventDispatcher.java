package it.polimi.se2019.utility;

public interface EventDispatcher {
    default void update(Event event){
        throw new UnsupportedOperationException("Cannot handle generic event");
    }
}
