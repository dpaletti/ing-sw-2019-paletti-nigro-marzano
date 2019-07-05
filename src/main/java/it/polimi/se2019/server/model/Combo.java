package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.utility.JsonHandler;
import it.polimi.se2019.commons.utility.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * This class defines the combos that can be used by the player throughout the game, for instance Run Around, Grab Stuff, Shoot People,..
 * Each combo is made up of a list of partial combos {@link it.polimi.se2019.server.model.PartialCombo}.
 */

public class Combo implements Jsonable{
    private String name;
    //RUNAROUND, GRABSTUFF, SHOOTPEOPLE, FRENZYMOVERELOADSHOOT, FRENZYMOVEFOURSQUARES, FRENZYMOVEGRAB, FRENZYMOVETWICEGRABSHOOT, FRENZYMOVETHRICESHOOT
    private List<PartialCombo> partialCombos;

    public String getName() {
        return name;
    }

    public Combo(String path){
    try {
        Combo combo = (Combo) JsonHandler.deserialize(new String(Files.readAllBytes(Paths.get(path))));
        partialCombos=combo.getPartialCombos();
        name=combo.getName();
    }catch (IOException c){
        Log.severe("Combo not found in given directory");
    }catch (NullPointerException e){
        Log.severe("Combo not created: ");
    }catch (ClassNotFoundException e){
        Log.severe("Error in json file, type");
    }
    }

    public Combo(Combo combo){
        this.name=combo.getName();
        this.partialCombos=combo.getPartialCombos();
    }

    public List<PartialCombo> getPartialCombos() {
        return partialCombos;
    }

    @Override
    public String toString() {
        return "Combo{" +
                "name='" + name + '\'' +
                ", partialCombos=" + partialCombos +
                '}';
    }
}
