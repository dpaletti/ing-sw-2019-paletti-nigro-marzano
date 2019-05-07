package it.polimi.se2019.network;

import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvents.DisconnectionEvent;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.SynchronousQueue;

public class ConnectionRMI implements Connection{
    private SynchronousQueue<String> in = new SynchronousQueue<>();
    private SynchronousQueue<String> out = new SynchronousQueue<>();

    private String username = null;
    private String password = null;

    Timer timer = new Timer();
    TimerTask timeout = new TimerTask() {
            @Override
            public void run() {
                try {
                    DisconnectionEvent disconnectionEvent = new DisconnectionEvent(username);
                    in.put(JsonHandler.serialize(disconnectionEvent, disconnectionEvent.getClass().toString().replace("class ", "")));
                }catch(InterruptedException e){
                    Log.severe(e.getMessage());
                    Thread.currentThread().interrupt();
                }

            }
        };


    public ConnectionRMI(String username, String password){
        this.username = username;
        this.password = password;
        timer.schedule(timeout, 400);
        //TODO make this configurable and compliant with various game phases
    }

    @Override
    public String getId() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public void setId(String username) {
        throw new UnsupportedOperationException("Cannot set username in RMI connection");
    }

    @Override
    public void setPassword(String password){
        throw new UnsupportedOperationException("Cannot set password in RMI connection");
    }

    @Override
    public String retrieve() {
        try {
            return in.take();
        }catch (InterruptedException e){
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public void submit(String data) {
        try {
            out.put(data);
        }catch (InterruptedException e){
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void ping(){
        timer.cancel();
        timer.purge();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    DisconnectionEvent disconnectionEvent = new DisconnectionEvent(username);
                    in.put(JsonHandler.serialize(disconnectionEvent, disconnectionEvent.getClass().toString().replace("class ", "")));
                }catch(InterruptedException e){
                    Log.severe(e.getMessage());
                    Thread.currentThread().interrupt();
                }

            }
        }, 200);
        //TODO make it configurable

    }

    public Queue<String> getOut() {
        return out;
    }

    public Queue<String> getIn() {
        return in;
    }

}
