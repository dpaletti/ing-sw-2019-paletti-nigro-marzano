package it.polimi.se2019.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public abstract class Weapon implements Serializable {
    private Ammo cardColour;
    private Set<Ammo> price;
    private Set<WeaponEffect> weaponEffects;
    private String name;
    private Integer maxheight;
    private Set<Set<Effect>> invalidCombinations;
    private transient Boolean loaded;
    private transient GraphNode<Effect> staticDefinition;


    public Ammo getCardColour() { return cardColour; }

    public Boolean getLoaded() { return loaded; }

    public Set<Ammo> getPrice() { return price; }

    public GraphNode<Effect> getStaticDefinition() { return staticDefinition; }

    public void setCardColour(Ammo cardColour) { this.cardColour = cardColour; }

    public void setLoaded(Boolean loaded) { this.loaded = loaded; }

    public void setPrice(Set<Ammo> price) {
        this.price = price;
    }

    public Set<WeaponEffect> getWeaponEffects() {
        return weaponEffects;
    }

    public void setWeaponEffects(Set<WeaponEffect> weaponEffects) {
        this.weaponEffects = weaponEffects;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMaxheight(Integer maxheight) {
        this.maxheight = maxheight;
    }

    public Integer getMaxheight() {
        return maxheight;
    }

    public void setInvalidCombinations(Set<Set<Effect>> invalidCombinations) {
        this.invalidCombinations = invalidCombinations;
    }

    public Set<Set<Effect>> getInvalidCombinations() {
        return invalidCombinations;
    }

    private Set<Effect> generateAllEffectsSet(){
        Set<Effect> allEffects= new HashSet<>();
        while (weaponEffects.iterator().hasNext()){
            while(weaponEffects.iterator().next().getEffects().iterator().hasNext()){
                allEffects.add(weaponEffects.iterator().next().getEffects().iterator().next());
            }
        }
        return allEffects;
    }

    public void generateGraph(){
        generateCombinations(generateAllEffectsSet(),staticDefinition,maxheight);
    }

    private void generateCombinations(Set<Effect> effects, GraphNode<Effect> radix, int maxheight){
        if(!effects.isEmpty() && maxheight!=0){
            while(effects.iterator().hasNext()){
                GraphNode<Effect> child= new GraphNode<>();
                Effect current;
                current=effects.iterator().next();
                //The radix is the "root" node of the graph
                if(radix.getNode().isEmpty()){
                    Set<Effect> childEffects= new HashSet<>();
                    childEffects.add(effects.iterator().next());
                    child=radix.insert(childEffects);
                }else{
                    //The radix is not the "root" node of the graph, so i have to check for repetitions in the graph
                    Set<Effect> childEffects= new HashSet<>();
                    //All the effects from the radix are inserted in the child
                    while(radix.getNode().iterator().hasNext()){
                        childEffects.add(radix.getNode().iterator().next());
                    }
                    //Now the effect from the effects is added to childEffects
                    childEffects.add(current);
                    //If a node with childEffect does not exist i can create it
                    if(!radix.getRoot().isIn(childEffects)){
                        child=radix.insert(childEffects);
                    }
                }
                //Creating another Set without the effect used
                Set<Effect> newSet= new HashSet<>();
                for(Effect effect : effects){
                    newSet.add(effect);
                }
                newSet.remove(current);
                generateCombinations(newSet,child, maxheight-1);
            }
        }
    }
}
