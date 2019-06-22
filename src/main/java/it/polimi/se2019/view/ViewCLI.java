package it.polimi.se2019.view;


import it.polimi.se2019.model.mv_events.SetUpEvent;
import it.polimi.se2019.network.Client;

import java.util.List;

public class ViewCLI extends View {

    public ViewCLI(Client client){
        super(client);
    }

    @Override
    public void matchMaking(List<String> usernames) {

    }

    @Override
    public void addPlayer(String username) {

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
