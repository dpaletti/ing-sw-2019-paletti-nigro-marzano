package it.polimi.se2019.model;

import java.util.Set;

public class LootCard {
    private Set<Ammo> ammo;
    private PowerUp powerUp;

    public void setAmmo(Set<Ammo> ammo) {
        this.ammo = ammo;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public Set<Ammo> getAmmo() {
        return ammo;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

}
