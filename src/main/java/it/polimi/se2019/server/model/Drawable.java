package it.polimi.se2019.server.model;

public interface Drawable {
    static void fillDeck(){
        throw new UnsupportedOperationException("Impossible to create a deck with this drawable");
    }
    String getName();
    //marker interface
}
