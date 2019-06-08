package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Combo {
    private String name;
    //RUNAROUND, GRABSTUFF, SHOOTPEOPLE, FRENZYMOVERELOADSHOOT, FRENZYMOVEFOURSQUARES, FRENZYMOVEGRAB, FRENZYMOVETWICEGRABSHOOT, FRENZYMOVETHRICESHOOT
    private List<PartialCombo> partialCombos;

    public String getName() {
        return name;
    }

    public Combo(String path){  //TODO: change this into names (weapons, powerups, combos)
    try {
        Combo combo = (Combo) JsonHandler.deserialize(new String(Files.readAllBytes(Paths.get(path))));
        partialCombos=combo.getPartialCombos();
    }catch (IOException c){
        Log.severe("Combo not found in given directory");
    }catch (NullPointerException e){
        Log.severe("Combo not created: ");
    }catch (ClassNotFoundException e){
        Log.severe("Error in json file, type");
    }
    }

    public List<PartialCombo> getPartialCombos() {
        return partialCombos;
    }

}
