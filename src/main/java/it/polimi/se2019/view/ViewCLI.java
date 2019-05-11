package it.polimi.se2019.view;


import it.polimi.se2019.model.mv_events.HandshakeEndEvent;
import it.polimi.se2019.network.Client;
import it.polimi.se2019.utility.MVEventDispatcher;

public class ViewCLI extends View {
    private Client client;
    private Dispatcher dispatcher = new Dispatcher();

    public ViewCLI(Client client){
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
        message.handle(dispatcher);
    }
}
