package it.polimi.se2019.view;

import com.google.gson.*;
import it.polimi.se2019.controller.MatchMakingController;
import it.polimi.se2019.network.Connection;
import it.polimi.se2019.network.ConnectionType;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualView extends View {
    private Server server;
    private List<Connection> connectionList = new CopyOnWriteArrayList<>();
    private List<Connection> timeOuts = new CopyOnWriteArrayList<>();
    private List<EventLoop> eventLoops = new CopyOnWriteArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();


    public VirtualView(Server server){
        super();
        register(new MatchMakingController(this));
        this.server = server;
    }

    public class EventLoop implements Runnable{
        Scanner in;
        InetAddress ip;

        public EventLoop(Scanner in, InetAddress ip){
            this.in = in;
            this.ip = ip;

        }
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    deserialize(in.nextLine());
                }catch(NoSuchElementException e){
                    Log.info("Client " + ip + " just disconnected");
                    VirtualView.this.notify(new DisconnectionEvent(ip));
                    break;
                }
            }
        }

        public InetAddress getIp() {
            return ip;
        }

        public Scanner getIn() {
            return in;
        }
    }

    public void newEventLoop (InetAddress ip, Scanner in){
        EventLoop eventLoop = new EventLoop(in, ip);
        eventLoops.add(eventLoop);
        executorService.submit(eventLoop);
    }


    public String serialize(MVEvent mvEvent, String type){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JsonElement jsonElement = gson.toJsonTree(mvEvent);
        jsonElement.getAsJsonObject().addProperty("type", type);
        return gson.toJson(jsonElement);
    }

    public void deserialize(String data){
        Log.fine("Data to be de-serialized: " + data);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(data).getAsJsonObject();
        String type = obj.get("type").getAsString();
        Log.fine("Type to convert: " + type);
        obj.remove("type");
        try {
            Class<?> classType = Class.forName(type);

            Object event = classType.cast(gson.fromJson(data, classType));
            notify((VCEvent)event);
        }catch (ClassNotFoundException e){
            Log.severe("ClassNotFoundException: " + e.getMessage());
        }
    }

    public void addPlayer(JoinEvent clientInfo) {
        if (clientInfo.getConnectionType().equals(ConnectionType.SOCKET)) {
            try {
                PrintWriter out = getCorrespondingPrintWriter(clientInfo.getSource());
                EventLoop eventLoop = getCorrespondingEventLoop(clientInfo.getSource());
                connectionList.add(
                        new Connection(
                                clientInfo.getConnectionType(),
                                out,
                                eventLoop));
            } catch (NullPointerException e) {
                Log.severe("NullPointerException: " + e.getMessage());
            }
        }
    }

    private PrintWriter getCorrespondingPrintWriter(InetAddress ip){
        PrintWriter out = null;
        for (Socket s:
                server.getSocketBuffer()){
            if(ip.equals(s.getInetAddress()))
                try{
                    out = new PrintWriter(s.getOutputStream(), true);
                }catch (IOException e){
                    Log.severe(e.getMessage());
                }
        }
        if(out == (null))
            throw new NullPointerException("MatchMaking request arrived from a Client connected through Socket" +
                    " that is not registered in the server SocketBuffer");
        return out;
    }

    private EventLoop getCorrespondingEventLoop(InetAddress ip){
        EventLoop eventLoop = null;
        for (EventLoop e:
                eventLoops) {
            if(e.getIp().equals(ip))
                eventLoop = e;
        }
        if(eventLoop == (null))
            throw new NullPointerException("No Eventloop running for this thread, you arrived here with Vodoo Magic");
        return eventLoop;

    }

    public void timeOut(InetAddress inetAddress){
        for (Connection c:
             connectionList) {
            if(c.getIp().equals(inetAddress)){
                connectionList.remove(c);
                timeOuts.add(c);
            }
        }
    }

    public List<Connection> getConnectionList() {
        return new ArrayList<>(connectionList);
    }

    public List<Connection> getTimeOuts(){
        return new ArrayList<>(timeOuts);
    }

    public void kick(InetAddress ip){
        server.kick(ip);
    }
}
