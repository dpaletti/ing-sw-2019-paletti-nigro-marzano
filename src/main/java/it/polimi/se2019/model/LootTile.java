package it.polimi.se2019.model;

import java.util.Map;
import java.util.Set;

public class LootTile extends Tile{
    @Override
    public WeaponSpot showWeaponSpot() {
        return super.showWeaponSpot();
    }

    @Override
    public LootCard getLootCard() {
        return super.getLootCard();
    }

    @Override
    public Map<Direction, Boolean> getDoors() {
        return super.getDoors();
    }

    @Override
    public RoomColour getColour() {
        return super.getColour();
    }

    @Override
    public Set<Figure> getFigures() {
        return super.getFigures();
    }

    @Override
    public TileType getTileType() {
        return super.getTileType();
    }

    @Override
    public Weapon getWeapon(Weapon weapon) {
        return super.getWeapon(weapon);
    }

    @Override
    public WeaponSpot getWeaponSpot() {
        return super.getWeaponSpot();
    }

    @Override
    public void setColour(RoomColour colour) {
        super.setColour(colour);
    }

    @Override
    public void setDoors(Map<Direction, Boolean> doors) {
        super.setDoors(doors);
    }

    @Override
    public void setFigures(Set<Figure> figures) {
        super.setFigures(figures);
    }

    @Override
    public void setWeaponSpot(WeaponSpot weaponSpot) {
        super.setWeaponSpot(weaponSpot);
    }
}

