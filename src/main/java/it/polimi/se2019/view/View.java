package it.polimi.se2019.view;

import it.polimi.se2019.model.Adrenaline;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;

public abstract class View extends Observable<Event> implements Observer<Adrenaline>{
    @Override
    public void update(Adrenaline message) {

    }
}
