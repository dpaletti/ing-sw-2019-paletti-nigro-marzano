package it.polimi.se2019.utility;


import it.polimi.se2019.view.VCEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestJsonHandler {
    //TODO json test files

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
}
