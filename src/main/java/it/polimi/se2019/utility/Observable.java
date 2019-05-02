package it.polimi.se2019.utility;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
    protected List<Observer<T>> observers;

    public void register(Observer<T> observer){
        observers.add(observer);
    }

    public void deregister(Observer<T> observer){
        observers.remove(observer);
    }

    protected void notify(T message){
        for (Observer<T> o: observers
             ) {
            o.update(message);
        }
    }

    public List<Observer<T>> getObservers() {
        return new ArrayList<>(observers);
    }
}
