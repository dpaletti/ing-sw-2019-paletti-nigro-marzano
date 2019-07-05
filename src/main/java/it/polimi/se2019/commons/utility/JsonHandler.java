package it.polimi.se2019.commons.utility;

import com.google.gson.*;
import it.polimi.se2019.commons.utility.Event;

/**
 * This class allows serialization and deserialization of events.
 */

public final class JsonHandler {

    private JsonHandler(){}

    public static String serialize(Event event){
        if(event == (null))
            throw new NullPointerException("Cannot serialize null event");
        GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();
        Gson gson = builder.create();
        JsonElement jsonElement = gson.toJsonTree(event);
        jsonElement.getAsJsonObject().addProperty("type", event.getClass().toString().replace("class ", ""));
        return gson.toJson(jsonElement);
    }

    public static Object deserialize(String data) throws ClassNotFoundException{
        if(data == (null))
            throw new NullPointerException("Cannot deserialize null data");
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(data).getAsJsonObject();
        String type = obj.get("type").getAsString();
        obj.remove("type");
        Class<?> classType = Class.forName(type);
        return classType.cast(gson.fromJson(data, classType));
    }
}
