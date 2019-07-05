package it.polimi.se2019.server.model;

/**
 * This marker interface implements methods for weapons and loot cards and power ups.
 */

public interface Drawable {
    static void fillDeck(){
        throw new UnsupportedOperationException("Impossible to create a deck with this drawable");
    }
    String getName();
    //marker interface
}
