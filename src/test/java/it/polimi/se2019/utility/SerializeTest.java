package it.polimi.se2019.utility;

import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.vc_events.VcJoinEvent;
import org.junit.Test;

public class SerializeTest {
    @Test
    public void test(){
        Log.fine(new VcJoinEvent("source", "username").toString());
    }
}
