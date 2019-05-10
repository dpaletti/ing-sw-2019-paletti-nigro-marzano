package it.polimi.se2019.model;

import it.polimi.se2019.utility.Log;

import java.util.HashSet;
import java.util.Set;

public abstract class Card {
    private Ammo cardColour;
    private Set<Ammo> price;
    private Set<WeaponEffect> weaponEffects;
    private String name;
    private Integer maxheight;
    private Set<Set<String>> invalidCombinations;
    private GraphNode<Effect> staticDefinition;

    public Integer getMaxheight() {
        return maxheight;
    }

    public void setMaxheight(Integer maxheight) {
        this.maxheight = maxheight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<WeaponEffect> getWeaponEffects() {
        return weaponEffects;
    }

    public void setWeaponEffects(Set<WeaponEffect> weaponEffects) {
        this.weaponEffects = weaponEffects;
    }

    public Ammo getCardColour() {
        return cardColour;
    }

    public void setCardColour(Ammo cardColour) {
        this.cardColour = cardColour;
    }

    public GraphNode<Effect> getStaticDefinition() {
        return staticDefinition;
    }

    public void setStaticDefinition(GraphNode<Effect> staticDefinition) {
        this.staticDefinition = staticDefinition;
    }

    public Set<Ammo> getPrice() {
        return price;
    }

    public void setPrice(Set<Ammo> price) {
        this.price = price;
    }

    public Set<Set<String>> getInvalidCombinations() {
        return invalidCombinations;
    }

    public void setInvalidCombinations(Set<Set<String>> invalidCombinations) {
        this.invalidCombinations = invalidCombinations;
    }

    private Set<Effect> generateAllEffectsSet(){
        Set<Effect> allEffects= new HashSet<>();
        while (weaponEffects.iterator().hasNext()){
            allEffects.addAll(weaponEffects.iterator().next().getEffects());
        }
        return allEffects;
    }

    public void generateGraph(){
        generateCombinations(generateAllEffectsSet(),staticDefinition,maxheight);
    }

    private void generateCombinations(Set<Effect> effects, GraphNode<Effect> radix, int maxheight){
        try{
            while(effects.iterator().hasNext()){
                Effect current=effects.iterator().next();
                //The radix is the "root" node of the graph
                if(radix.getNode().isEmpty()){
                    Set<Effect> childEffects= new HashSet<>();
                    childEffects.add(current);
                    GraphNode<Effect> child =radix.insert(childEffects);
                    Set<Effect> newSet= new HashSet<>(effects);
                    newSet.remove(current);
                    generateCombinations(newSet,child,maxheight-1);
                }else{
                    //The radix is not the "root" node of the graph, so i have to check for repetitions in the graph
                    //All the effects from the radix are inserted in the child
                    Set<Effect> childEffects= new HashSet<>(radix.getNode());
                    //Now the effect from the effects is added to childEffects
                    childEffects.add(current);
                    //If a node with childEffect does not exist i can create it
                    if(!radix.getRoot().isIn(childEffects)){
                        GraphNode<Effect> child=radix.insert(childEffects);
                        //Creating another Set without the effect used
                        Set<Effect> newSet= new HashSet<>(effects);
                        newSet.remove(current);
                        generateCombinations(newSet,child,maxheight-1);
                    }
                }

            }
        }catch (NullPointerException e){
            Log.severe("NullPointer on the Effect Set");
        }
    }

    //TODO Write method to eliminate invalid Combinations

}
