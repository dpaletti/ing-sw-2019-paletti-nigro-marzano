package it.polimi.se2019.model;

import it.polimi.se2019.utility.Log;

import java.util.HashSet;
import java.util.Set;

public class GraphWeaponEffect extends GenericWeaponEffect {
    private GraphNode<PartialWeaponEffect> effectGraph=new GraphNode<>(null);

    public GraphNode<PartialWeaponEffect> getEffectGraph() {
        return effectGraph;
    }


    public GraphWeaponEffect(WeaponEffect weaponEffect){
        this.priority=weaponEffect.priority;
        this.invalidEffects=weaponEffect.invalidEffects;
        this.name=weaponEffect.name;
        this.price=weaponEffect.price;
        this.maxHeight=weaponEffect.maxHeight;
        generateGraphPartial(weaponEffect.getEffects(),effectGraph,this.maxHeight,weaponEffect.getEffects());
    }

    private void generateGraphPartial(Set<PartialWeaponEffect> effectSet, GraphNode<PartialWeaponEffect> root, int maxHeight,Set<PartialWeaponEffect> allEffects){
        if (maxHeight==0)
            return;
        int actualMax=0;
        for (PartialWeaponEffect effect : effectSet) {
            if (effect.priority > actualMax)
                actualMax = effect.priority;
        }
        Set<PartialWeaponEffect> tempSet=new HashSet<>(effectSet);
        for (PartialWeaponEffect effect : tempSet) {
            if (effect.priority == actualMax) {
                GraphNode<PartialWeaponEffect> node = new GraphNode<>(effect);
                root.addChild(node);
                for (String string : effect.invalidEffects)
                    effectSet.remove(getEffect(string,allEffects));
                effectSet.remove(effect);
                generateGraphPartial(effectSet, node,maxHeight--,allEffects);
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
