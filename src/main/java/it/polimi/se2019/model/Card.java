package it.polimi.se2019.model;

import it.polimi.se2019.utility.Log;
import java.util.HashSet;
import java.util.Set;

public abstract class Card {
    private Ammo cardColour;
    private Set<Ammo> price;
    private Set<WeaponEffect> weaponEffects;
    private String name;
    private Integer maxHeight;
    private Set<Set<String>> invalidCombinations;
    private GraphNode<Effect> staticDefinition= new GraphNode<>();

    public Card (String name){
        this.name= name;
    }

    public Card (String name, AmmoColour ammoColour){
        this.name= name;
        this.cardColour= new Ammo(ammoColour);
    }
    public Integer getMaxHeight() {
        return maxHeight;
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
        for (WeaponEffect weaponEffect: weaponEffects){
            allEffects.addAll(weaponEffect.getEffects());
        }
        return allEffects;
    }

    public void generateGraph(){
        if(staticDefinition.getChildren().isEmpty()) {
            generateCombinations(generateAllEffectsSet(), staticDefinition, maxHeight);
            eliminateInvalidCombinations();
        }else{
            throw new UnsupportedOperationException("static definition has been generated");
        }
    }

    private void generateCombinations(Set<Effect> effects, GraphNode<Effect> radix, int maxheight){
        try{
            for (Effect effect : effects){
                //The radix is the "root" node of the graph
                if(radix.getNode().isEmpty()){
                    Set<Effect> childEffects= new HashSet<>();
                    childEffects.add(effect);
                    GraphNode<Effect> child =radix.insert(childEffects);
                    Set<Effect> newSet= new HashSet<>(effects);
                    newSet.remove(effect);
                    generateCombinations(newSet,child,maxheight-1);
                }else{
                    //The radix is not the "root" node of the graph, so i have to check for repetitions in the graph
                    //All the effects from the radix are inserted in the child
                    Set<Effect> childEffects= new HashSet<>(radix.getNode());
                    //Now the effect from the effects is added to childEffects
                    childEffects.add(effect);
                    //If a node with childEffect does not exist i can create it
                    if(!radix.getRoot().isIn(childEffects)){
                        GraphNode<Effect> child=radix.insert(childEffects);
                        //Creating another Set without the effect used
                        Set<Effect> newSet= new HashSet<>(effects);
                        newSet.remove(effect);
                        generateCombinations(newSet,child,maxheight-1);
                    }else{
                        try {
                            radix.addChild(radix.getRoot().getGraphNode(childEffects));
                        }catch (NullPointerException e){
                            //Can't throw an exception here the control was did in the previous if
                        }

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
    private Effect getEffect(String stringName) throws ClassNotFoundException {
        for (Effect e : generateAllEffectsSet()) {
            if (e.getName().equals(stringName)) {
                return e;
            }
        }
        throw  new ClassNotFoundException("There is not such effect in the weapon");
    }

    //This method eliminates the invalidCombination of the weapon from the staticDefinition graph
    private void eliminateInvalidCombinations(){
        staticDefinition.removeAll(getInvalidNodes());
    }

    private Set<GraphNode<Effect>> getInvalidNodes(){
        Set<GraphNode<Effect>> invalidSet= new HashSet<>();
        for (Set<Effect> set: buildInvalidSets()){
            try {
                invalidSet.add(staticDefinition.getGraphNode(set));
            }catch (NullPointerException e){
                Log.severe("There is not such node in the static Definition graph");
            }
        }
        return invalidSet;
    }

}
