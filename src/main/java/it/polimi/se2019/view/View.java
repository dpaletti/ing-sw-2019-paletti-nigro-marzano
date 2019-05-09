package it.polimi.se2019.view;
import it.polimi.se2019.model.mv_events.UsernameEvaluationEvent;
import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.vc_events.HandshakeEndEvent;

import java.util.ArrayList;

public abstract class View extends Observable<VCEvent> implements Observer<MVEvent>{

    protected View(){
        observers = new ArrayList<>();
    }

    protected class ClientViewDispatcher extends MVEventDispatcher{
        @Override
        public void update(UsernameEvaluationEvent message) {
            View.this.notify(new HandshakeEndEvent());
        }
    }
    
}
