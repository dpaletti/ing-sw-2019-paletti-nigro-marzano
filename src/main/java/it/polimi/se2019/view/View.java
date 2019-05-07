package it.polimi.se2019.view;
import it.polimi.se2019.network.Connection;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;

import java.util.ArrayList;

public abstract class View extends Observable<VCEvent> implements Observer<MVEvent>{

    protected View(){
        observers = new ArrayList<>();
    }
    
}
