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
    private GraphNode<Effect> staticDefinition= new GraphNode<>();

    public Integer getMaxheight() {
        return maxheight;
    }

    public String getName() {
        return name;
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

    public GraphNode<Effect> getStaticDefinition() {
        return staticDefinition;
    }

    public Set<Ammo> getPrice() {
        return price;
    }

    public Set<Set<String>> getInvalidCombinations() {
        return invalidCombinations;
    }

    private Set<Effect> generateAllEffectsSet(){
        Set<Effect> allEffects= new HashSet<>();
        while (weaponEffects.iterator().hasNext()){
            allEffects.addAll(weaponEffects.iterator().next().getEffects());
        }
        return allEffects;
    }

    public void generateGraph(){
        if(staticDefinition.getChildren().isEmpty()) {
            generateCombinations(generateAllEffectsSet(), staticDefinition, maxheight);
            eliminateInvalidCombinations();
        }else{
            throw new UnsupportedOperationException("static definition has been generated");
        }
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

    //This method builds an invalidCombination Set<Set<Effect>> from a Set<Set<String>>
    private Set<Set<Effect>> buildInvalidSets(){
        Set<Set<Effect>> obj= new HashSet<>();
        for(Set<String> combination : invalidCombinations){
            Set<Effect> newSet= new HashSet<>();
            for(String effectName: combination){
                try {
                    newSet.add(getEffect(effectName));
                }catch (ClassNotFoundException e){
                    Log.severe("One of the string in the invalidCombinations was not found in the WeaponEffects");
                }
            }
            obj.add(newSet);
        }
        return obj;
    }

    //This method returns the effect in the weapon with the specified name, if there is not such effect it throws an exception
    private Effect getEffect(String name) throws ClassNotFoundException {
        for (Effect e : generateAllEffectsSet()) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        throw  new ClassNotFoundException("There is not such effect in the weapon");
    }

    //This method eliminates the invalidCombination of the weapon from the staticDefinition graph
    private void eliminateInvalidCombinations(){
        for (Set<Effect> set: buildInvalidSets()){
            try {
                staticDefinition.remove(set);
            }catch (ClassNotFoundException e){
                Log.severe("There is not such node in the static Definition graph");
            }
        }
    }

}
