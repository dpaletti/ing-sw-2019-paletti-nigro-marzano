package it.polimi.se2019.server.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class generalizes all the decks used in the game and it contains both a regular deck and one with all the discards
 * as those can be used whenever the regular deck is over.
 */

public class Deck{
    private List<Drawable> deck =  new ArrayList<>();
    private List<Drawable> discards = new ArrayList<>();

    public Deck(List<Drawable> cards){
        deck.addAll(cards);
        Collections.shuffle(deck);
    }

    public Drawable draw(){
        if(deck.isEmpty()) {
            deck = discards;
            Collections.shuffle(deck);
            discards.clear();
        }
        return deck.remove(deck.size() - 1);
    }

    public void discard (Drawable card){
        discards.add(card);
    }
}
