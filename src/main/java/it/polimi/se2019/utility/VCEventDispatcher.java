package it.polimi.se2019.utility;

import it.polimi.se2019.view.vc_events.*;
import it.polimi.se2019.view.VCEvent;

public class VCEventDispatcher implements Observer<VCEvent>{

    @Override
    public void update(VCEvent message) {
        throw new UnsupportedOperationException("Generic VC Event not supported");
    }

    public void update(JoinEvent message){
        throw new UnsupportedOperationException("Join Event not supported");
    }

    public void update(DisconnectionEvent message){
        throw new UnsupportedOperationException("Disconnection event not supported");
    }

    public void update(ChosenEffectEvent message){
        throw new UnsupportedOperationException("ChosenEffect event not supported");
    }

    public void update (DefineTeleportPositionEvent message){
        throw new UnsupportedOperationException("DefineTeleportPosition event not supported");
    }
}
