package it.polimi.se2019.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LootTile extends Tile{

    public LootTile (RoomColour colour, Map<Direction, Boolean> doors, Set<Figure> figures, WeaponSpot weaponSpot, LootCard loot, Point position, List<Tear> hp){
        super(colour, doors, figures, weaponSpot, loot, position, hp);
    }

    @Override
    public LootCard getLootCard() {
        return loot;
    }

    @Override
    public TileType getTileType() {
        return TileType.LOOTTILE;
    }

    @Override
    public Weapon getWeapon(Weapon weapon){ //eccezione sempre
        return super.getWeapon(weapon);
    }
}


