package it.polimi.se2019.utility;

import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.view.*;


public class MVEventDispatcher implements Observer<MVEvent> {

    @Override
    public void update(MVEvent message) {
        throw new UnsupportedOperationException("Generic MVEvent not supported");
    }

    public void update(EffectToApplyEvent message){
        throw new UnsupportedOperationException("EffectToApplyEvent not supported");
    }

    public void update(FigureToAttackEvent message){
        throw new UnsupportedOperationException("FigureToAttackEvent not supported");
    }

    public void update(NotEnoughAmmoEvent message){
        throw new UnsupportedOperationException("NotEnoughAmmoEvent not supported");
    }

    public void update(TeleportEvent message){
        throw new UnsupportedOperationException("TeleportEvent not supported");
    }

    public void update(WeaponToGrabEvent message){
        throw new UnsupportedOperationException("WeaponToGrabEvent not supported");
    }

    public void update(WeaponToLeaveEvent message){
        throw new UnsupportedOperationException("WeaponToLeaveEvent not supported");
    }

    public void update(UsernameDeletionEvent message){
        throw new UnsupportedOperationException("UsernameDeletionEvent not supported");
    }

    public void update(MatchMakingEndEvent message){
        throw new UnsupportedOperationException("MatchMakingEndEvent not supported");
    }

    public void update(HandshakeEndEvent message){
        throw new UnsupportedOperationException("HandshakeEndEvent not supported");
    }

    public void update(JoinMatchMakingEvent message){
        throw new UnsupportedOperationException("JoinMatchMakingEvent not supported");
    }

    public void update(ConnectionRefusedEvent message){
        throw new UnsupportedOperationException("ConnectionRefusedEvent not supported");
    }

    public void update(PausePlayerEvent message){
        throw new UnsupportedOperationException("PausePlayerEvent not supported");
    }
}
