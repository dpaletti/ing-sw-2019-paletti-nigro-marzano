package it.polimi.se2019.model;

import it.polimi.se2019.utility.Log;
import java.util.HashSet;
import java.util.Set;

public abstract class Card {
    private Ammo cardColour;
    private Set<Ammo> price;
    private Set<WeaponEffect> weaponEffects;
    private String name;
    private GraphNode<GraphWeaponEffect> staticDefinition;
    private int maxHeight;

    public Card (String name){
        this.name= name;
    }

    public Card (String name, AmmoColour ammoColour){
        this.name= name;
        this.cardColour= new Ammo(ammoColour);
    }

    private GraphWeaponEffect found;

    public GraphNode<GraphWeaponEffect> getWeapon (){
        return staticDefinition;
    }

    public GraphWeaponEffect getWeaponEffect (String name){
        found = null;
        searchWeaponEffectByName(name, staticDefinition);
        return found;
    }

    private void searchWeaponEffectByName (String name, GraphNode<GraphWeaponEffect> tGraphNode){
        if (found!=null)
            return;

        if (tGraphNode.getKey().getName().equals(name)){
            found = tGraphNode.getKey();
            searchWeaponEffectByName(name, tGraphNode);
        }
        tGraphNode.visited = true;
        for (GraphNode<GraphWeaponEffect> graphNode : tGraphNode.getChildren()) {
            if (!graphNode.visited && found==null) {
                searchWeaponEffectByName(name,graphNode);
            }
        }
    }


    public String getName() {
        return name;
    }

    public Set<WeaponEffect> getWeaponEffects() {
        return weaponEffects;
    }

    public Ammo getCardColour() {
        return cardColour;
    }



    public Set<Ammo> getPrice() {
        return price;
    }


    public Set<PartialWeaponEffect> generateAllEffectsSet(){
        Set<PartialWeaponEffect> allPartialWeaponEffects = new HashSet<>();
        for (WeaponEffect weaponEffect: weaponEffects){
            allPartialWeaponEffects.addAll(weaponEffect.getEffects());
        }
        return allPartialWeaponEffects;
    }

   private void defineCard(){
        Set<GraphWeaponEffect> graphWeaponEffects= new HashSet<>();
        for (WeaponEffect weaponEffect: weaponEffects){
            GraphWeaponEffect graphWeaponEffect= new GraphWeaponEffect(weaponEffect);
            graphWeaponEffects.add(graphWeaponEffect);
        }
        generateGraph(graphWeaponEffects,staticDefinition,maxHeight,maxHeight);
   }

    private void generateGraph(Set<GraphWeaponEffect> effectSet, GraphNode<GraphWeaponEffect> root, int maxHeight,int constantMaxHeight){
        if (maxHeight==0)
            return;
        int actualMax=0;
        for (GraphWeaponEffect effect : effectSet) {
            if (effect.priority > actualMax)
                actualMax = effect.priority;
        }
        for (GraphWeaponEffect effect : effectSet) {
            Set<GraphWeaponEffect> childrenSet= new HashSet<>(effectSet);
            if (effect.priority == actualMax) {
                GraphNode<GraphWeaponEffect> node = new GraphNode<>(effect,constantMaxHeight-maxHeight+1);
                root.addChild(node);
                for (String string : effect.invalidEffects)
                    childrenSet.remove(getEffect(string, effectSet));
                childrenSet.remove(effect);
                int newHeight=maxHeight-1;
                generateGraph(childrenSet, node,newHeight,constantMaxHeight);
            }
        }
    }


    private GraphWeaponEffect getEffect(String name,Set<GraphWeaponEffect> effectSet){
        for(GraphWeaponEffect effect: effectSet){
            if (effect.getName().equals(name))
                return effect;
        }
        throw new NullPointerException("Can't find effect");
    }

   public GraphNode<GraphWeaponEffect> getStaticDefinition() {
        if(staticDefinition==null) {
            staticDefinition= new GraphNode<>(null,0);
            defineCard();
            return staticDefinition;
        }else{
            return staticDefinition;
        }
    }

}
