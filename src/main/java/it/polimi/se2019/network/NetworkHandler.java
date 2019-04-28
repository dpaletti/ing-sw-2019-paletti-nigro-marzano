package it.polimi.se2019.network;

import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;

import java.net.Socket;

public class NetworkHandler extends Observable<MVEvent> implements Observer<VCEvent> {
    Socket socket;

    public NetworkHandler(Socket socket){

    }

    @Override
    public void update(VCEvent message) {

    }
}
