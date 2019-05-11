package it.polimi.se2019.view;

import it.polimi.se2019.model.mv_events.HandshakeEndEvent;
import it.polimi.se2019.network.Client;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.MVEventDispatcher;

public class ViewGUI extends View {
    private Client client;
    private Dispatcher dispatcher = new Dispatcher();

    public ViewGUI(Client client){
        this.client = client;
    }

    private class Dispatcher extends MVEventDispatcher {
        @Override
        public void update(HandshakeEndEvent message) {
            client.setSessionToken(message.getDestination());
            client.usernameSelection(message.getUsernames());
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
