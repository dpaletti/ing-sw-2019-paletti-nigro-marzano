package it.polimi.se2019.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck{
    private List<Drawable> deck =  new ArrayList<>();
    private List<Drawable> discards = new ArrayList<>();

    public Deck(List<Drawable> cards){
        deck.addAll(cards);
        Collections.shuffle(deck);
    }

    public Drawable draw(){
        return deck.remove(deck.size() - 1);
    }

    public void discard (Drawable card){
        discards.add(card);
    }
}
