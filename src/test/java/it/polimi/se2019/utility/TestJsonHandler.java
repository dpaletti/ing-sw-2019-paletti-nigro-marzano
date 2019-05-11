package it.polimi.se2019.utility;


import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.ChosenEffectEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestJsonHandler {
    @Test(expected = NullPointerException.class)
    public void testSerializeNull(){
        JsonHandler.serialize(null);
        JsonHandler.serialize(null);
        JsonHandler.serialize(new ChosenEffectEvent("source"));
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
        VCEvent event = new ChosenEffectEvent("source");
        String serialized = JsonHandler.serialize(event);
        try {
            VCEvent deserialized = (VCEvent) JsonHandler.deserialize(serialized);
            assertEquals(event.getSource(), deserialized.getSource());
        }catch (ClassNotFoundException e){
            //
        }

    }


}
