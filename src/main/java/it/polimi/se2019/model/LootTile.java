package it.polimi.se2019.model;

public class LootTile extends Tile{
    private LootCard loot;

    public LootCard getLoot() {
        return loot;
    }

    public void setLoot(LootCard loot) {
        this.loot = loot;
    }
}