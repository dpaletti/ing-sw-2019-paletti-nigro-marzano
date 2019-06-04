package it.polimi.se2019.utility;

import it.polimi.se2019.model.AllowedWeaponsEvent;
import it.polimi.se2019.model.PartialWeaponEffect;
import it.polimi.se2019.model.mv_events.*;


public interface MVEventDispatcher extends EventDispatcher {

    default void dispatch(MVSelectionEvent message){
        throw new UnsupportedOperationException("MVSelectionEvent not supported");
    }

    default void dispatch(FigureToAttackEvent message){
        throw new UnsupportedOperationException("FigureToAttackEvent not supported");
    }

    default void dispatch(NotEnoughAmmoEvent message){
        throw new UnsupportedOperationException("NotEnoughAmmoEvent not supported");
    }

    default void dispatch(TeleportEvent message){
        throw new UnsupportedOperationException("TeleportEvent not supported");
    }

    default void dispatch(WeaponToGrabEvent message){
        throw new UnsupportedOperationException("WeaponToGrabEvent not supported");
    }

    default void dispatch(WeaponToLeaveEvent message){
        throw new UnsupportedOperationException("WeaponToLeaveEvent not supported");
    }

    default void dispatch(UsernameDeletionEvent message){
        throw new UnsupportedOperationException("UsernameDeletionEvent not supported");
    }

    default void dispatch(MatchMakingEndEvent message){
        throw new UnsupportedOperationException("MatchMakingEndEvent not supported");
    }

    default void dispatch(HandshakeEndEvent message){
        throw new UnsupportedOperationException("HandshakeEndEvent not supported");
    }

    default void dispatch(MvJoinEvent message){
        throw new UnsupportedOperationException("MvJoinEvent not supported");
    }

    default void dispatch(ConnectionRefusedEvent message){
        throw new UnsupportedOperationException("ConnectionRefusedEvent not supported");
    }

    default void dispatch(MvReconnectionEvent message){
        throw new UnsupportedOperationException("MvReconnectionEvent not supported");
    }

    default void dispatch(SyncEvent message){
        throw new UnsupportedOperationException("SyncEvent not supported");
    }

    default void dispatch(TimerEvent message){
        throw new UnsupportedOperationException("TimerEvent not supported");
    }

    default void dispatch (FinalFrenzyStartingEvent message){
        throw new UnsupportedOperationException("FinalFrenzyStartingEvent not supported");
    }

    default void dispatch (MVDeathEvent message){
        throw new UnsupportedOperationException("MVDeathEvent not supported");
    }

    default void dispatch (MVMoveEvent message){
        throw new UnsupportedOperationException("MoveEvent not supported");
    }

    default void dispatch (TurnEvent message){
        throw new UnsupportedOperationException("TurnEvent not supported");
    }

    default void dispatch (StartFirstTurnEvent message){
        throw new UnsupportedOperationException("StartFirstTurnEvent not supported");
    }

    default void dispatch (AllowedMovementsEvent message){
        throw new UnsupportedOperationException("Allowed Movements Event not supported");
    }

    default void dispatch (PowerUpToLeaveEvent message){
        throw new UnsupportedOperationException("PowerUpToLeave Event not supported");
    }

    default void dispatch(AllowedWeaponsEvent message){
        throw new UnsupportedOperationException("AllowedWeaponsEvent not supported");
    }

    default void dispatch(UnpausedPlayerEvent message) {
        throw new UnsupportedOperationException("UnpausedPlayerEvent not supported");
    }

    default void dispatch(PausedPlayerEvent message){
        throw new UnsupportedOperationException("PausedPlayerEvent not supported");
    }

    default void dispatch (UpdatePointsEvent message){
        throw new UnsupportedOperationException("UpdatePointEvent not supported");
    }

    default void dispatch (WeaponEffectsEvent message){
        throw new UnsupportedOperationException("WeaponEffectsEvent not supported");
    }

    default void dispatch (PartialWeaponEffect message){
        throw new UnsupportedOperationException("PartialWeaponEffect not supported");
    }
}
