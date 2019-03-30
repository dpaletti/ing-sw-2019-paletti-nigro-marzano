package it.polimi.se2019.model;

import java.util.List;

public class LootCard {
    private List<Ammo> ammo;
    private PowerUp powerUp;

    public void setAmmo(List<Ammo> ammo) {
        this.ammo = ammo;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public List<Ammo> getAmmo() {
        return ammo;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

}
