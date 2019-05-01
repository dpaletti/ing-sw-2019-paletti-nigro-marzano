package it.polimi.se2019.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpawnTile extends Tile {

    public SpawnTile (RoomColour colour, Map<Direction, Boolean> doors, Set<Figure> figures, WeaponSpot weaponSpot, LootCard loot, Point position, List<Tear> hp){
        super(colour, doors, figures, weaponSpot, loot, position, hp);
    }
    @Override
    public Weapon getWeapon(Weapon weapon) {
        return super.getWeapon(weapon);
    }

    @Override
    public TileType getTileType() {
        return TileType.SPAWNTILE;
    }

    @Override
    public LootCard getLootCard() {
        return super.getLootCard();
    }//non è permesso restituire loot card da qui

}