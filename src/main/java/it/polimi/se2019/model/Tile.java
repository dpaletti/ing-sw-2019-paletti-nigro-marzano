package it.polimi.se2019.model;

import java.util.Map;
import java.util.Set;

public abstract class Tile {
    private RoomColour colour;
    private Map<Direction, Boolean> doors;
    private Set<Figure> figures;
    private WeaponSpot weaponSpot;

    public void setColour(RoomColour colour) {
        this.colour = colour;
    }

    public void setDoors(Map<Direction, Boolean> doors) {
        this.doors = doors;
    }

    public void setFigures(Set<Figure> figures) {
        this.figures = figures;
    }

    public void setWeaponSpot(WeaponSpot weaponSpot) { this.weaponSpot = weaponSpot; }

    public RoomColour getColour() {
        return colour;
    }

    public Map<Direction, Boolean> getDoors() {
        return doors;
    }

    public Set<Figure> getFigures() {
        return figures;
    }

    public WeaponSpot getWeaponSpot() { return weaponSpot; }

    public LootCard getLootCard(){}

    public WeaponSpot showWeaponSpot(){}

    public Weapon getWeapon(Weapon weapon){}

    public TileType getTileType(){}
}
