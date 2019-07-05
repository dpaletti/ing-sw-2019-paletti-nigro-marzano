package it.polimi.se2019.server.model;

import java.util.List;

/**
 * This class extends the Effect class {@link it.polimi.se2019.server.model.Effect} by adding useful properties such as the price of the weapon or power up effect.
 */

public abstract class GenericWeaponEffect extends Effect{
    protected List<Ammo> price;
    protected int maxHeight;
    protected int effectType;

    public List<Ammo> getPrice() {
        return price;
    }
}
