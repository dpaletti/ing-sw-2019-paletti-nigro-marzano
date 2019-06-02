package it.polimi.se2019.model;

import it.polimi.se2019.utility.Log;

import java.util.HashSet;
import java.util.Set;

public class GraphWeaponEffect extends GenericWeaponEffect {
    private GraphNode<PartialWeaponEffect> effectGraph=new GraphNode<>(null,0);

    public GraphNode<PartialWeaponEffect> getEffectGraph() {
        return effectGraph;
    }


    public GraphWeaponEffect(WeaponEffect weaponEffect){
        this.priority=weaponEffect.priority;
        this.invalidEffects=weaponEffect.invalidEffects;
        this.name=weaponEffect.name;
        this.price=weaponEffect.price;
        this.maxHeight=weaponEffect.maxHeight;
        generateGraphPartial(weaponEffect.getEffects(),effectGraph,this.maxHeight,this.maxHeight);
    }

    private void generateGraphPartial(Set<PartialWeaponEffect> effectSet, GraphNode<PartialWeaponEffect> root, int maxHeight,int constantMaxHeight){
        if (maxHeight==0)
            return;
        int actualMax=0;
        for (PartialWeaponEffect effect : effectSet) {
            if (effect.priority > actualMax)
                actualMax = effect.priority;
        }
        for (PartialWeaponEffect effect : effectSet) {
            Set<PartialWeaponEffect> childrenSet= new HashSet<>(effectSet);
            if (effect.priority == actualMax) {
                GraphNode<PartialWeaponEffect> node = new GraphNode<>(effect,constantMaxHeight-maxHeight);
                root.addChild(node);
                for (String string : effect.invalidEffects)
                    childrenSet.remove(getEffect(string, effectSet));
                childrenSet.remove(effect);
                generateGraphPartial(childrenSet, node,maxHeight--,constantMaxHeight);
            }
        }
    }




    private PartialWeaponEffect getEffect(String name,Set<PartialWeaponEffect> effectSet){
        Log.fine(name);
        for(PartialWeaponEffect effect: effectSet){
            Log.fine(effect.name);
            if (effect.getName().equals(name))
                return effect;
        }
        throw new NullPointerException("Can't find effect");
    }

    @Override
    public String toString() {
        return name;
    }
}
