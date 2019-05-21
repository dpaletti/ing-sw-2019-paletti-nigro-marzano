package it.polimi.se2019.model;

import it.polimi.se2019.utility.Factory;
import it.polimi.se2019.utility.Log;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PowerUpDeck implements Deck {
    private List<PowerUp> cards;

    @Override
    public void createCards(String directory) {
        try {
            for (File file : Paths.get(directory).toFile().listFiles()) {
                cards.add(Factory.createPowerUp(file.getPath()));
            }
        }catch (NullPointerException e){
            Log.severe("Path given does not exist");
        }
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    public List<PowerUp> getCards() {
        return cards;
    }

    public Card draw() {
        Random random= new Random();
        int number= random.nextInt(cards.size());
        PowerUp powerUp=cards.get(number);
        cards.remove(cards.get(number));
        return powerUp;
    }

}
