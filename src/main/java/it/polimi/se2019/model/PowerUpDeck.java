package it.polimi.se2019.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PowerUpDeck implements Deck {
    private List<PowerUp> deck = new ArrayList<>();

    public PowerUpDeck(){
        deck.addAll(CardHelper.getInstance().getAllPowerUp());
    }

    @Override
    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card draw() {
        Random random= new Random();
        int number= random.nextInt(deck.size());
        PowerUp powerUp=deck.get(number);
        deck.remove(deck.get(number));
        return powerUp;
    }

}
