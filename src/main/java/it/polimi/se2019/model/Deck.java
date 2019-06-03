package it.polimi.se2019.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck{
    private List<Drawable> deck =  new ArrayList<>();

    public Deck(List<Drawable> cards){
        deck.addAll(cards);
        Collections.shuffle(deck);
    }

    public void shuffle(){

    }

    public Drawable draw(){
        return deck.remove(deck.size() - 1);
    }

}
