package it.polimi.se2019.model;

import java.util.HashSet;
import java.util.Set;

public class LootCard implements Grabbable, Drawable,Jsonable{
    private String name;
    private Set<Ammo> ammo= new HashSet<>();

    public LootCard (LootCard lootCard){
        this.name= lootCard.name;
        this.ammo= new HashSet<>(lootCard.ammo);
    }

    public LootCard (String name, AmmoColour firstAmmoColour, AmmoColour secondAmmoColour, AmmoColour thirdAmmoColour){
        this.name = name;
        ammo.add(new Ammo(firstAmmoColour));
        ammo.add(new Ammo(secondAmmoColour));
        ammo.add(new Ammo(thirdAmmoColour));

    }

    public LootCard (String name, AmmoColour firstAmmoColour, AmmoColour secondAmmoColour){
        this.name = name;
        ammo.add(new Ammo(firstAmmoColour));
        ammo.add(new Ammo(secondAmmoColour));
    }

    public Set<Ammo> getAmmo() {
        return ammo;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Jsonable copy() {
        return new LootCard(this);
    }
}
