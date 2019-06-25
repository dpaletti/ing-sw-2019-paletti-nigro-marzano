package it.polimi.se2019.network;

import it.polimi.se2019.view.VirtualView;

import java.util.NoSuchElementException;

public class EventLoop implements Runnable {
    private Connection connection;
    private VirtualView virtualView;

    public EventLoop(VirtualView virtualView, Connection c) {
        this.connection = c;
        this.virtualView = virtualView;
    }


    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted())
                virtualView.retrieve(connection);
        } catch (NoSuchElementException e) {
            virtualView.disconnect(connection);
        }
    }
}
