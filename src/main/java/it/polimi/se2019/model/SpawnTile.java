package it.polimi.se2019.model;

import java.util.Map;

public class SpawnTile extends Tile {

    private WeaponSpot weaponSpot;

    @Override
    public Object grab() {
        return super.grab();
    }

    public WeaponSpot getWeaponSpot() {
        return weaponSpot;
    }

    public void setWeaponSpot(WeaponSpot weaponSpot) {
        this.weaponSpot = weaponSpot;
    }
}
