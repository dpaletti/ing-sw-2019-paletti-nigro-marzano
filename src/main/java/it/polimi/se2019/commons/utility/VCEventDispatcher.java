package it.polimi.se2019.commons.utility;

import it.polimi.se2019.commons.vc_events.*;

/**
 * This class dispatches all events arriving from the view and directed to the Controller to the correct Controller that
 * is able to handle them. See {@link it.polimi.se2019.commons.utility.EventDispatcher}.
 */

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

    default void dispatch (DiscardedPowerUpEvent message){
        throw new UnsupportedOperationException("Discarded PowerUp Event not supported");
    }

    default void dispatch (VCEndOfTurnEvent message){
        throw new UnsupportedOperationException("VCEndOfTurnEvent not supported");
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

    default void dispatch (ChosenEffectPowerUpEvent message){
        throw new UnsupportedOperationException("ChosenEffectPowerUp not supported");
    }

    default void dispatch (VCPartialEffectEvent message){
        throw new UnsupportedOperationException("VCPartialEffectEvent not supported");
    }

    default void dispatch (VCSellPowerUpEvent message){
        throw new UnsupportedOperationException("VCSellPowerUpEvent not supported");
    }

    default void dispatch (VCChooseAmmoToPayEvent message){
        throw new UnsupportedOperationException("VCChooseAmmoToPayEvent not supported");
    }

    default void dispatch (VCCardEndEvent message){
        throw new UnsupportedOperationException("VCChooseAmmoToPayEvent not supported");
    }
    default void dispatch(VCFinalFrenzy message){
        throw new UnsupportedOperationException("FinalFrenzy not supported");
    }
}
