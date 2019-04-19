package it.polimi.se2019.model;

import java.util.Map;
import java.util.Set;

public class SpawnTile extends Tile {
    @Override
    public WeaponSpot getWeaponSpot() {
        return super.getWeaponSpot();
    }

    @Override
    public Weapon getWeapon(Weapon weapon) {
        return super.getWeapon(weapon);
    }

    @Override
    public TileType getTileType() {
        return super.getTileType();
    }

    @Override
    public Set<Figure> getFigures() {
        return super.getFigures();
    }

    @Override
    public RoomColour getColour() {
        return super.getColour();
    }

    @Override
    public Map<Direction, Boolean> getDoors() {
        return super.getDoors();
    }

    @Override
    public LootCard getLootCard() {
        return super.getLootCard();
    }

    @Override
    public void setWeaponSpot(WeaponSpot weaponSpot) {
        super.setWeaponSpot(weaponSpot);
    }

    @Override
    public void setFigures(Set<Figure> figures) {
        super.setFigures(figures);
    }

    @Override
    public void setDoors(Map<Direction, Boolean> doors) {
        super.setDoors(doors);
    }

    @Override
    public void setColour(RoomColour colour) {
        super.setColour(colour);
    }

    @Override
    public WeaponSpot showWeaponSpot() {
        return super.showWeaponSpot();
    }
}

