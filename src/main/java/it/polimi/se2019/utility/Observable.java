package it.polimi.se2019.utility;

import java.util.List;

public class Observable<T> {
    private List<Observer<T>> observers;

    public void register(Observer<T> observer){}

    public void deregister(Observer<T> observer){}

    protected void notify(T message){}


}
