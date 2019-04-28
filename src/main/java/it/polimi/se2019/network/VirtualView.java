package it.polimi.se2019.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.*;

import java.net.Socket;
import java.util.logging.Logger;

public class VirtualView extends View {
    Server server;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public VirtualView(Server server){
        this.server = server;

    }

    @Override
    public void update(MVEvent message) {
            //TODO overload this with all MVEvents extending classes, to be done later
    }

    public void handleMessage(String data){
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        VCEvent vcEvent = gson.fromJson(data, it.polimi.se2019.view.VCEvent.class);
        for (Observer<VCEvent> o: observers) {
            o.update(vcEvent);
        }
    }

    public void handleDisconnection(Socket socket){
        for (Observer<VCEvent> o :
                observers
            ) {
            o.update(new DisconnectionEvent(socket));
        }
    }

    public void addConnection(Socket socket){
        for (Observer<VCEvent> o:
                observers
             ) {
            o.update(new ConnectionEvent(socket));
        }
    }
}
