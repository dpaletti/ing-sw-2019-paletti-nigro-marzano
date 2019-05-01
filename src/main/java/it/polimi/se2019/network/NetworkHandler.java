package it.polimi.se2019.network;

import com.google.gson.*;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;

import java.io.IOException;

public abstract class NetworkHandler extends Observable<MVEvent> implements Observer<VCEvent> {
    protected Client client;

    public NetworkHandler(Client client){
        this.client = client;
    }


    protected String serialize(VCEvent vcEvent, String type){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JsonElement jsonElement = gson.toJsonTree(vcEvent);
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
            notify((MVEvent)event);
        }catch (ClassNotFoundException e){
            Log.severe("ClassNotFoundException: " + e.getMessage());
        }
    }

    public abstract void submit(String toVirtualView);
    public abstract String retrieve();
    public abstract void enterMatchMaking();
    public abstract void listenToEvent();

    public abstract void establishConnection() throws IOException;
}
