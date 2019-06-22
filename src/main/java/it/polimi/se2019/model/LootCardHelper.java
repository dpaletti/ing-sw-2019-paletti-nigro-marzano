package it.polimi.se2019.model;

import java.util.HashSet;
import java.util.Set;

public class LootCardHelper extends JsonHelper{

    public LootCardHelper() { this.create();}

    @Override
    public void create() {
        for (AmmoColour topAmmoColour: AmmoColour.values()){
            for (AmmoColour bottomAmmoColour: AmmoColour.values()){
                if (!topAmmoColour.equals(bottomAmmoColour)) {
                    for (int i = 0; i < 3; i++) {
                        char[] name = {topAmmoColour.toString().charAt(0),
                                bottomAmmoColour.toString().charAt(0),
                                bottomAmmoColour.toString().charAt(0)};
                        String nameOfCard = new String(name);
                        LootCard lootCard = new LootCard(nameOfCard, topAmmoColour, bottomAmmoColour, bottomAmmoColour);
                        helped.add(lootCard);
                    }
                }
                char[] name = {'P',
                        topAmmoColour.toString().charAt(0),
                        bottomAmmoColour.toString().charAt(0)};
                String nameOfCard= new String(name);
                LootCard lootCard= new LootCard(nameOfCard, topAmmoColour, bottomAmmoColour);
                helped.add(lootCard);
            }
        }
    }

    public Set<LootCard> getLootCards(){
        Set<LootCard> lootCards= new HashSet<>();
        for (Jsonable j: this.getAll()){
            lootCards.add((LootCard) j);
        }
        return lootCards;
    }
}
