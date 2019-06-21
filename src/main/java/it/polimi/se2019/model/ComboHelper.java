package it.polimi.se2019.model;

import it.polimi.se2019.utility.Log;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ComboHelper {
    private static ComboHelper instance = null;
    private static Set<Combo> comboSet = new HashSet<>();

    private ComboHelper(){
        try {
            for (String name : Paths.get("files/combos").toFile().list()) {
                comboSet.add((new Combo(Paths.get("files/combos/".concat(name)).toString())));
            }
        }catch (NullPointerException e){
            Log.severe("Combo not found in the combos directory");
        }
    }

    public static ComboHelper getInstance() {
        if (instance == null){
            instance= new ComboHelper();
        }
        return instance;
    }

    public Combo findComboByName(String name) {
        for (Combo c: comboSet) {
            if (name.equalsIgnoreCase(c.getName())) {
                return c;
            }
        }
        throw new NullPointerException("Combo not found");
    }
}
