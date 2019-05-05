package it.polimi.se2019.utility;

import com.google.gson.*;
import it.polimi.se2019.view.Event;

public final class JsonHandler {

    private JsonHandler(){}

    public static String serialize(Event event, String type){

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        JsonElement jsonElement = gson.toJsonTree(event);
        jsonElement.getAsJsonObject().addProperty("type", type);
        return gson.toJson(jsonElement);
    }

    public static Object deserialize(String data) throws ClassNotFoundException{
        //TODO better error handling
        //probably better to propagate down ClassNotFoundException
        Log.fine("Data to be de-serialized: " + data);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(data).getAsJsonObject();
        String type = obj.get("type").getAsString();
        Log.fine("Type to convert: " + type);
        obj.remove("type");
        Class<?> classType = Class.forName(type);
        return classType.cast(gson.fromJson(data, classType));
    }
}