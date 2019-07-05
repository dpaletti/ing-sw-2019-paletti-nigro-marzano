package it.polimi.se2019.server.network;

import it.polimi.se2019.commons.utility.Log;

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
            Log.fine("EventLoop for room: " + virtualView.getRoomNumber() + "and" + connection + "dying");
        } catch (NoSuchElementException e) {
            virtualView.disconnect(connection);
        } catch (IndexOutOfBoundsException e){
            Log.severe("IndexOutOfBound in room: " + virtualView.getRoomNumber() + " on Connection: " + connection + " " + e.getMessage());
            e.printStackTrace();
            //run();
        }
    }
}
