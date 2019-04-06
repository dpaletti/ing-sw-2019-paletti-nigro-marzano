package it.polimi.se2019.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Weapon {
    private Ammo cardColour;
    private Set<Ammo> price;
    private List<Pair<Integer, Ammo>> optionalPrice;
    private Boolean loaded;
    private TreeNode<Pair<List<List<Action>>, List<Ammo>>> storedActions;

    public abstract
    TreeNode<Pair<Map<Player, List<Action>>, List<Ammo>>>
    effect (Player player);

    protected abstract
    Set<Player>
    generateTargetSet (Player player);

    protected abstract Set<Player> getVisibleTargets(Player player);
    protected abstract Set<Player> getDifferentTargets(Set<Player> attackedPlayers);
    protected abstract Set<Player> getZoneTargets(Set<Tile> Zone);
    protected abstract Set<Player> getRadiusGreaterThanTargets(Integer radius);
    protected abstract Set<Player> getRadiusEqualsTargets(Integer radius);


    public Ammo getCardColour() {
        return cardColour;
    }

    public Boolean getLoaded() {
        return loaded;
    }

    public List<Pair<Integer, Ammo>> getOptionalPrice() {
        return optionalPrice;
    }

    public Set<Ammo> getPrice() {
        return price;
    }

    public void setCardColour(Ammo cardColour) {
        this.cardColour = cardColour;
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

    public void setOptionalPrice(List<Pair<Integer, Ammo>> optionalPrice) {
        this.optionalPrice = optionalPrice;
    }

    public void setPrice(Set<Ammo> price) {
        this.price = price;
    }
}
