package it.polimi.se2019.utility;

import it.polimi.se2019.model.mv_events.AllowedWeaponsEvent;
import it.polimi.se2019.model.mv_events.*;


public interface MVEventDispatcher extends EventDispatcher {

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

    default void dispatch(SetUpEvent message){
        throw new UnsupportedOperationException("SetUpEvent not supported");
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


    default void dispatch(MatchConfigurationEvent message){
        throw new UnsupportedOperationException("MatchConfigurationEvent not supported");
    }

    default void dispatch (AllowedMovementsEvent message){
        throw new UnsupportedOperationException("Allowed Movements Event not supported");
    }

    default void dispatch (PowerUpToLeaveEvent message){
        throw new UnsupportedOperationException("PowerUpToLeave Event not supported");
    }

    default void dispatch(UnpausedPlayerEvent message) {
        throw new UnsupportedOperationException("UnpausedPlayerEvent not supported");
    }

    default void dispatch(PausedPlayerEvent message){
        throw new UnsupportedOperationException("PausedPlayerEvent not supported");
    }

    default void dispatch (UpdateMarkEvent message){
        throw new UnsupportedOperationException("UpdateMarkEvent not supported");
    }

    default void dispatch (UpdateHpEvent message){
        throw new UnsupportedOperationException("UpdateHpEvent not supported");
    }

    default void dispatch (UpdatePointsEvent message){
        throw new UnsupportedOperationException("UpdatePointsEvent not supported");
    }


    default void dispatch (PartialEffectEvent message){
        throw new UnsupportedOperationException("PartialEffectEvent not supported");
    }

    default void dispatch (MVWeaponEndEvent message){throw new UnsupportedOperationException("MVWeaponEndEvent not supported");}

    default void dispatch (GrabbablesEvent message){
        throw new UnsupportedOperationException("GrabbablesEvent not supported");
    }

    default void dispatch(GrabbedWeaponEvent message){
        throw new UnsupportedOperationException("GrabbedWeaponEvent not supported");
    }

    default void dispatch(DrawnPowerUpEvent message){
        throw new UnsupportedOperationException("DrawnPowerUpEvent not supported");
    }

    default void dispatch(NewAmmosEvent message){
        throw new UnsupportedOperationException("NewAmmosEvent not supported");
    }

    default void dispatch (NotEnoughPlayersConnectedEvent message){
        throw new UnsupportedOperationException("NotEnoughPlayersConnectedEvent not supported");
    }

    default void dispatch(UsablePowerUpEvent message){
        throw new UnsupportedOperationException("UsablePowerUpEvent not supported");
    }

    default void dispatch(ReloadableWeaponsEvent message){
        throw new UnsupportedOperationException("Reloadable weapons not supported");
    }

    default void dispatch(AllowedWeaponsEvent message){
        throw new UnsupportedOperationException("AllowedWeapon not supported");
    }

    default void dispatch(PossibleEffectsEvent message){
        throw new UnsupportedOperationException("PossibleEffects not supported");
    }
    default void dispatch(PartialSelectionEvent message){
        throw new UnsupportedOperationException("PartialSelectionEvent not supported");
    }

    default void dispatch (MVSellPowerUpEvent message){
        throw new UnsupportedOperationException("MVSellPowerUpEvent not supported");
    }

    default void dispatch (FinanceUpdateEvent message){
        throw new UnsupportedOperationException("FinanceUpdateEvent not supported");
    }

    default void dispatch (MVEndOfTurnEvent message){
        throw new UnsupportedOperationException("MVEndOfTurnEvent not supported");
    }

    default void dispatch (MVChooseAmmoToPayEvent message){
        throw new UnsupportedOperationException("MVChooseAmmoToPayEvent not supported");
    }

    default void dispatch (DisablePowerUpEvent message){
        throw new UnsupportedOperationException("DisablePowerUpEvent not supported");
    }

    default void dispatch (MVRespawnEvent message){
        throw new UnsupportedOperationException("MVRespawnEvent not supported");
    }

    default void dispatch (BoardRefreshEvent message){
        throw new UnsupportedOperationException("BoardRefreshEvent not supported");
    }

    default void dispatch (GrabbedLootCardEvent message){
        throw new UnsupportedOperationException("GrabbedLootCardEvent not supported");
    }
}
