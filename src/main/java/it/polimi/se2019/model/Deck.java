package it.polimi.se2019.model;


public interface Deck{
    void createCards(String directory);
    void shuffle();
    Card draw ();
}
