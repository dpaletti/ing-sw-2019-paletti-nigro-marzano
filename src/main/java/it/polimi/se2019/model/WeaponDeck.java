package it.polimi.se2019.model;

import java.util.*;

public class WeaponDeck implements Deck {
    private List<Weapon> deck = new ArrayList<>();
    private Random random= new Random();

    public WeaponDeck(){
        deck.addAll(CardHelper.getInstance().getAllWeapons());
    }

    @Override
    public void shuffle() {
        Collections.shuffle(deck);
    }

    @Override
    public Card draw() {
        int number= random.nextInt(deck.size());
        Weapon weapon=deck.get(number);
        deck.remove(deck.get(number));
        return weapon;
    }
}
