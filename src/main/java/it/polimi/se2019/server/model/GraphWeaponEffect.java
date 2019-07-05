package it.polimi.se2019.server.model;

/**
 * This class extends Generic Weapon Effect {@link it.polimi.se2019.server.model.GenericWeaponEffect} by adding a partial effect to it.
 * As a matter of fact, each weapon or power up effect is made up of smaller effects that have a target specification {@link it.polimi.se2019.server.model.TargetSpecification}
 * that defines their effect.
 */

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
        this.effectType=weaponEffect.effectType;
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
