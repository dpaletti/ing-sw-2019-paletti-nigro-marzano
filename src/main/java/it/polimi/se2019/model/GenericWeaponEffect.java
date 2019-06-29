package it.polimi.se2019.model;

import java.util.List;

public abstract class GenericWeaponEffect extends Effect{
    protected List<Ammo> price;
    protected int maxHeight;
    protected int effectType;

    public List<Ammo> getPrice() {
        return price;
    }
}
