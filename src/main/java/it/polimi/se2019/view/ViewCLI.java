package it.polimi.se2019.view;


import it.polimi.se2019.model.mv_events.ConnectionRefusedEvent;
import it.polimi.se2019.model.mv_events.HandshakeEndEvent;
import it.polimi.se2019.model.mv_events.MatchMakingEndEvent;
import it.polimi.se2019.network.Client;
import it.polimi.se2019.utility.MVEventDispatcher;

public class ViewCLI extends View {
    private Dispatcher dispatcher = new Dispatcher();

    public ViewCLI(Client client){
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
        message.handle(dispatcher);
    }
}
