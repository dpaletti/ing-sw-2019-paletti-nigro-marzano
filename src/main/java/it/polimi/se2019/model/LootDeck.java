package it.polimi.se2019.model;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LootDeck implements Deck {
    List<LootCard> deck;

    public void createDeck() {
        deck.addAll(CardHelper.getInstance().getAllLootCards());
    }

    @Override
    public void shuffle() {
        Collections.shuffle(deck);
    }

    @Override
    public Card draw() {
        Random random= new Random();
        int number= random.nextInt(deck.size());
        LootCard lootCard= deck.get(number);
        deck.remove(deck.get(number));
        return lootCard;
    }

}
