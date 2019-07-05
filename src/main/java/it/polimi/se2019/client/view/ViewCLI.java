package it.polimi.se2019.client.view;


import it.polimi.se2019.client.network.Client;
import it.polimi.se2019.commons.mv_events.SetUpEvent;

import java.util.List;


public class ViewCLI extends View {

    public ViewCLI(Client client){
        super(client);
    }

    @Override
    public void matchMaking(List<String> usernames, List<String> configs) {

    }

    @Override
    public void addPlayer(String username, int missingPlayers) {

    }


        @Override
        public void dispatch(SetUpEvent message){
            //TODO stuff
        }

    @Override
    public void update(MVEvent message) {
        message.handle(this);
    }
}
