package it.polimi.se2019.utility;


import it.polimi.se2019.view.VCEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestJsonHandler {
    @Test(expected = NullPointerException.class)
    public void testSerializeNull(){
        JsonHandler.serialize(null, null);
        JsonHandler.serialize(null, "type");
        JsonHandler.serialize(new VCEvent(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeserializeNull(){
        try {
            JsonHandler.deserialize(null);
        }catch (ClassNotFoundException e){
            //
        }
    }

    @Test
    public void testSerializeDeserialize(){
        VCEvent event = new VCEvent("source");
        String serialized = JsonHandler.serialize(event, event.getClass().toString().replace("class ", ""));
        try {
            VCEvent deserialized = (VCEvent) JsonHandler.deserialize(serialized);
            assertEquals(event.getSource(), deserialized.getSource());
        }catch (ClassNotFoundException e){
            //
        }

    }


}
