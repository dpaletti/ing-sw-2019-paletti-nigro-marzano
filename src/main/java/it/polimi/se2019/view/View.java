package it.polimi.se2019.view;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;

public abstract class View extends Observable<VCEvent> implements Observer<MVEvent>{
    @Override
    public void update(MVEvent message) {
        //TODO see weather every View implementation needs its
    }
}
