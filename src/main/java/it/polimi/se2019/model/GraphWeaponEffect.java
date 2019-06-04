package it.polimi.se2019.model;

public class GraphWeaponEffect extends GenericWeaponEffect {
    private GraphNode<PartialWeaponEffect> effectGraph=new GraphNode<>(null,0);

    public GraphNode<PartialWeaponEffect> getEffectGraph() {
        return effectGraph;
    }


    public GraphWeaponEffect(WeaponEffect weaponEffect, Card card){
        this.priority=weaponEffect.priority;
        this.invalidEffects=weaponEffect.invalidEffects;
        this.name=weaponEffect.name;
        this.price=weaponEffect.price;
        this.maxHeight=weaponEffect.maxHeight;
        card.generateGraph(weaponEffect.getEffects(), effectGraph, weaponEffect.maxHeight, weaponEffect.maxHeight);
    }

    @Override
    public String toString() {
        return name;
    }

    public int getEffectType(){
        return effectType;
    }
}
