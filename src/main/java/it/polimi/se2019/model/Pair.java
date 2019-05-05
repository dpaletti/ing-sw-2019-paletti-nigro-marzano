package it.polimi.se2019.model;

import java.io.Serializable;

public class Pair<T, S> implements Serializable {
    private T first;
    private S second;
    public Pair(T first, S second){
        this.first = first;
        this.second = second;
    }

    public void setFirst(T first){
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}
