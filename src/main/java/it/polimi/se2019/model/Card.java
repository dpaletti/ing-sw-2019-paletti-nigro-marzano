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
        generateGraph(graphWeaponEffects,staticDefinition,maxHeight);
   }

    private void generateGraph(Set<GraphWeaponEffect> effectSet, GraphNode<GraphWeaponEffect> root, int maxHeight){
        if (maxHeight==0)
            return;
        int actualMax=0;
        for (GraphWeaponEffect effect : effectSet) {
            if (effect.priority > actualMax)
                actualMax = effect.priority;
        }
        Set<GraphWeaponEffect> tempSet= new HashSet<>(effectSet);
        for (GraphWeaponEffect effect : tempSet) {
            if (effect.priority == actualMax) {
                GraphNode<GraphWeaponEffect> node = new GraphNode<>(effect);
                root.addChild(node);
                for (String string : effect.invalidEffects)
                    effectSet.remove(getEffect(string, effectSet));
                effectSet.remove(effect);
                generateGraph(effectSet, node,maxHeight--);
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




    //TODO this is the method to refactor
   /* private void generateCombinations(Set<PartialWeaponEffect> partialWeaponEffects, GraphNode<PartialWeaponEffect> radix, int maxheight){
        if (maxheight==0){
            return;
        }
        for (PartialWeaponEffect partialWeaponEffect : partialWeaponEffects) {
            //The radix is the "root" node of the graph
            if (radix.getNode().isEmpty()) {
                Set<PartialWeaponEffect> childPartialWeaponEffects = new HashSet<>();
                childPartialWeaponEffects.add(partialWeaponEffect);
                GraphNode<PartialWeaponEffect> child = radix.insert(childPartialWeaponEffects);
                Set<PartialWeaponEffect> newSet = new HashSet<>(partialWeaponEffects);
                newSet.remove(partialWeaponEffect);
                generateCombinations(newSet, child, maxheight - 1);
            } else {
                //The radix is not the "root" node of the graph, so i have to check for repetitions in the graph
                //All the partialWeaponEffects from the radix are inserted in the child
                Set<PartialWeaponEffect> childPartialWeaponEffects = new HashSet<>(radix.getNode());
                //Now the partialWeaponEffect from the partialWeaponEffects is added to childPartialWeaponEffects
                childPartialWeaponEffects.add(partialWeaponEffect);
                //If a node with childEffect does not exist i can create it
                if (!radix.getRoot().isIn(childPartialWeaponEffects)) {
                    GraphNode<PartialWeaponEffect> child = radix.insert(childPartialWeaponEffects);
                    //Creating another Set without the partialWeaponEffect used
                    Set<PartialWeaponEffect> newSet = new HashSet<>(partialWeaponEffects);
                    newSet.remove(partialWeaponEffect);
                    generateCombinations(newSet, child, maxheight - 1);
                } else {
                    try {
                        radix.addChild(radix.getRoot().getGraphNode(childPartialWeaponEffects));
                    } catch (NullPointerException e) {
                        Log.severe("Null pointer exception in getGraphNode");
                    }
                }
            }

        }
    }*/

   public GraphNode<GraphWeaponEffect> getStaticDefinition() {
        if(staticDefinition==null) {
            staticDefinition= new GraphNode<>(null);
            defineCard();
            return staticDefinition;
        }else{
            return staticDefinition;
        }
    }

}
