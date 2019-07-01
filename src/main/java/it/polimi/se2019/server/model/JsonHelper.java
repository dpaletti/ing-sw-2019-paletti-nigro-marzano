package it.polimi.se2019.server.model;

import java.util.HashSet;
import java.util.Set;

public abstract class JsonHelper {
    protected Set<Jsonable> helped=new HashSet<>();

    //To use this method the return object has to be casted to the correct type
    public Jsonable findByName(String name){
        for (Jsonable j: helped){
            if(j.getName().equalsIgnoreCase(name))
                return j;
        }
        throw new NullPointerException("Not found");
    }

    public Set<Jsonable> getAll(){
        return new HashSet<>(helped);
    }

    public abstract void create();
}
