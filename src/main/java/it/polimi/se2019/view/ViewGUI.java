package it.polimi.se2019.view;

import it.polimi.se2019.model.mv_events.MatchMakingEndEvent;
import it.polimi.se2019.network.Client;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.MVEventDispatcher;

public class ViewGUI extends View {
    private Dispatcher dispatcher = new Dispatcher();

    public ViewGUI(Client client){
        super(client);
    }

    private class Dispatcher extends CommonDispatcher {

        @Override
        public void update(MatchMakingEndEvent message){
            //TODO stuff
        }
    }

    @Override
    public void update(MVEvent message) {
        try {
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            Log.severe("Unsupported event in view");
            throw new UnsupportedOperationException("Error: " + e.getMessage(), e);
        }
    }
}
