package it.polimi.se2019.model;

import it.polimi.se2019.utility.Log;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ComboHelper extends JsonHelper {
    public ComboHelper() {
        this.create();
    }

    @Override
    public void create() {
        try {
            for (String n : Paths.get("files/combos").toFile().list()) {
                helped.add((new Combo(Paths.get("files/combos/".concat(n)).toString())));
            }
        }catch (NullPointerException e){
            Log.severe("Combo not found in the combos directory");
        }
    }

    public Set<Combo> getPowerUps(){
        Set<Combo> combos= new HashSet<>();
        for (Jsonable j: this.getAll()){
            combos.add((Combo)j);
        }
        return combos;
    }

}

