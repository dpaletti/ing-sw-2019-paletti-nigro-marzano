package it.polimi.se2019.model;

import java.util.HashSet;
import java.util.Set;

public class LootCard extends Card {
    private Set<Ammo> ammo= new HashSet<>();

    public LootCard (String name, AmmoColour firstAmmoColour, AmmoColour secondAmmoColour, AmmoColour thirdAmmoColour){
        super(name);
        ammo.add(new Ammo(firstAmmoColour));
        ammo.add(new Ammo(secondAmmoColour));
        ammo.add(new Ammo(thirdAmmoColour));

    }

    public LootCard (String name, AmmoColour firstAmmoColour, AmmoColour secondAmmoColour){
        super (name);
        ammo.add(new Ammo(firstAmmoColour));
        ammo.add(new Ammo(secondAmmoColour));
    }

    public Set<Ammo> getAmmo() {
        return ammo;
    }
}
