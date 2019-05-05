package it.polimi.se2019.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public abstract class Weapon implements Serializable {
    protected Ammo cardColour;
    protected Set<Ammo> price;
    private transient Boolean loaded;
    private transient GraphNode<Effect> staticDefinition;
    private String weaponName;


    public Ammo getCardColour() { return cardColour; }

    public Boolean getLoaded() { return loaded; }

    public Set<Ammo> getPrice() { return price; }

    public GraphNode<Effect> getStaticDefinition() { return staticDefinition; }

    public String getWeaponName() {
        return weaponName;
    }

    public void setCardColour(Ammo cardColour) { this.cardColour = cardColour; }

    public void setLoaded(Boolean loaded) { this.loaded = loaded; }

    public void setPrice(Set<Ammo> price) {
        this.price = price;
    }

    public void setStaticDefinition(GraphNode<Effect> staticDefinition) { this.staticDefinition = staticDefinition; }

    public void setWeaponName(String weaponName) {
        this.weaponName = weaponName;
    }
}
