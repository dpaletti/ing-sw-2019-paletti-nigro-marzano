package it.polimi.se2019.utility;

import it.polimi.se2019.view.vc_events.EndOfTurnEvent;
import it.polimi.se2019.view.vc_events.*;

public interface VCEventDispatcher extends EventDispatcher {

    default void dispatch(VcJoinEvent message){
        throw new UnsupportedOperationException("Join Event not supported");
    }

    default void dispatch(DisconnectionEvent message){
        throw new UnsupportedOperationException("Disconnection event not supported");
    }

    default void dispatch(ChosenEffectEvent message){
        throw new UnsupportedOperationException("ChosenEffect event not supported");
    }

    default void dispatch(VcReconnectionEvent message){
        throw new UnsupportedOperationException("Reconnection event not supported");
    }

    default void dispatch (ReloadEvent message){
        throw new UnsupportedOperationException("Reload event not supported");
    }

    default void dispatch (VCMoveEvent message){
        throw new UnsupportedOperationException("Move event not supported");
    }

    default void dispatch (GrabEvent message){
        throw new UnsupportedOperationException("Grab event not supported");
    }

    default void dispatch (ChosenWeaponEvent message){
        throw new UnsupportedOperationException("Chosen Weapon Event not supported");
    }

    default void dispatch (SpawnEvent message){
        throw new UnsupportedOperationException("Respawn Event not supported");
    }

    default void dispatch (PowerUpUsageEvent message){
        throw new UnsupportedOperationException("PowerUp Usage Event not supported");
    }

    default void dispatch (ActionEvent message){
        throw new UnsupportedOperationException("MVSelectionEvent not supported");
    }

    default void dispatch (DiscardedPowerUpEvent message){
        throw new UnsupportedOperationException("Discarded PowerUp Event not supported");
    }

    default void dispatch (EndOfTurnEvent message){
        throw new UnsupportedOperationException("EndOfTurnEvent not supported");
    }

    default void dispatch (ShootEvent message){
        throw new UnsupportedOperationException("ShootEvent not supported");
    }

    default void dispatch (VCSelectionEvent message) {
        throw new UnsupportedOperationException("VCSelectionEvent not supported");
    }

    default void dispatch (ConflictResolutionEvent message){
        throw new UnsupportedOperationException("ConflictResolutionEvent not supported");
    }

    default void dispatch (VCWeaponEndEvent message){
        throw new UnsupportedOperationException("VCWeaponEndEvent not supported");
    }

    default void dispatch (CalculatePointsEvent message){
        throw new UnsupportedOperationException("CalculatePointsEvent not supported");
    }

    default void dispatch (VcMatchConfigurationEvent message){
        throw new UnsupportedOperationException("VcMatchConfigurationEvent not supported");
    }

    default void dispatch (ChosenComboEvent message){
        throw new UnsupportedOperationException("ChosenComboEvent not supported");
    }
}
