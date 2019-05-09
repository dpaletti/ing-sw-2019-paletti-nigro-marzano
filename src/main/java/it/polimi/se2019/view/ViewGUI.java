package it.polimi.se2019.view;


import it.polimi.se2019.model.mv_events.UsernameEvaluationEvent;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.MVEventDispatcher;

public class ViewGUI extends View {
    Dispatcher dispatcher = new Dispatcher();

    public ViewGUI(){
        super();
    }

    private class Dispatcher extends ClientViewDispatcher{
    }

    @Override
    public void update(MVEvent message) {
        try {
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException("Error: " + e.getMessage(), e);
        }
    }
}
