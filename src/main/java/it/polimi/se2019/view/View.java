package it.polimi.se2019.view;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;

public abstract class View extends Observable<Event> implements Observer<Game>{
    @Override
    public void update(Game message) {

    }
}
