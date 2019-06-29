package it.polimi.se2019.view;

import it.polimi.se2019.utility.EventDispatcher;
import it.polimi.se2019.view.ui_events.*;

public interface UiDispatcher extends EventDispatcher {
    default void dispatch(UiAddPlayer message){
        throw new UnsupportedOperationException("UiAddPlayer Unsupported");
    }
    default void dispatch(UiTimerStart message){
        throw new UnsupportedOperationException("UiTimerStart Unsupported");
    }
    default void dispatch(UiTimerStop message){
        throw new UnsupportedOperationException("UiTimerStop Unsupported");
    }
    default void dispatch(UiTimerTick message){
        throw new UnsupportedOperationException("UiTimerTick Unsupported");
    }
    default void dispatch(UiRemovePlayer message){
        throw new UnsupportedOperationException("UiRemovePlayer Unsupported");
    }
    default void dispatch(UiCloseMatchMaking message){
        throw new UnsupportedOperationException("UiCloseMatchMaking Unsupported");
    }
    default void dispatch(UiInitialize message){
        throw new UnsupportedOperationException("GuiInitialized Unsupported");
    }
    default void dispatch(UiHighlightTileEvent message){
        throw new UnsupportedOperationException("UiHighlightTileEvent unsupported");
    }
    default void dispatch(UiMoveFigure message){
        throw new UnsupportedOperationException("GuiMoveFigureEvent unsupported");
    }

    default void dispatch(UiMapConfigEvent message){
        throw new UnsupportedOperationException("UiMapConfigEvent unsupported");
    }

    default void dispatch(UiCloseSetup message){
        throw new UnsupportedOperationException("UiCloseSetupEvent unsupported");
    }

    default void dispatch(UiBoardInitialization message){
        throw new UnsupportedOperationException("UiBoardInitialization unsupported");
    }

    default void dispatch(UiShowWeapon message){
        throw new UnsupportedOperationException("UiShowWeapon unsupported");
    }
    default void dispatch (UiHideWeapon message){
        throw new UnsupportedOperationException("UiHideWeapon unsupported");
    }
    default void dispatch(UiContextSwitch message){
        throw new UnsupportedOperationException("UiContextSwitch not supported");
    }
    default void dispatch(UiHidePlayers message){
        throw new  UnsupportedOperationException("UiHideplayers not supported");
    }
    default void dispatch(UiLockPlayers message){
        throw new UnsupportedOperationException("UiLockPlayers not supported");
    }
    default void dispatch(UiShowPlayers message){
        throw new UnsupportedOperationException("UiShowPlayers not supported");
    }
    default void dispatch(UiUnlockPlayers message){
        throw new UnsupportedOperationException("UiUnlockPlayers not supported");
    }

    default void dispatch(UiSetPlayerBoard message){
        throw new UnsupportedOperationException("UiSetPlayerBoard not supported");
    }
    default void dispatch(UiPutPowerUp message){
        throw new UnsupportedOperationException("UiPutPowerUp not supported");
    }
    default void dispatch(UiPutWeapon message){
        throw new UnsupportedOperationException("UiPutWeapon not supported");
    }
    default void dispatch(UiPausePlayer message){
        throw new UnsupportedOperationException("UiPausePlayer not supported");
    }
    default void dispatch(UiSpawn message){
        throw new UnsupportedOperationException("UiSpawn not supported");
    }
    default void dispatch(UiAvailablePowerup message){
        throw new UnsupportedOperationException("UiAvailablePowerup not supported");
    }
    default void dispatch(UiAvailableMove message){
        throw new UnsupportedOperationException("UiAvailableMove not supported");
    }
    default void dispatch(UiResetMap message){
        throw new UnsupportedOperationException("UiResetMap not supported");
    }
    default void dispatch(UiTurnEnd message){
        throw new UnsupportedOperationException("UiTurnEnd not supported");
    }
    default void dispatch(UiStartTurn message){
        throw new UnsupportedOperationException("UiStartTurn not supported");
    }
    default void dispatch(UiGrabWeapon message){
        throw new UnsupportedOperationException("UiGrabWeapon not supported");
    }
    default void dispatch(UiGrabLoot message){
        throw new UnsupportedOperationException("UiGrabLoot not supported");
    }
    default void dispatch(UiShowFourth message){
        throw new UnsupportedOperationException("UiShowFourth not supported");
    }
    default void dispatch(UiActivateWeapons message){
        throw new UnsupportedOperationException("UiActivateWeapons not supported");
    }
    default void dispatch(UiActivateWeaponEffects message){
        throw new UnsupportedOperationException("UiActivateWeaponEffects not supported");
    }
    default void dispatch(UiHighlightPlayer message){
        throw new UnsupportedOperationException("UiHighlighPlayer not supported");
    }
    default void dispatch(UiSellPowerups message){
        throw new UnsupportedOperationException("UiSellPowerups not supported");
    }
    default void dispatch(UiRemoveMarks message){
        throw new UnsupportedOperationException("UiRemoveMarks not supported");
    }
    default void dispatch(UiAddDamage message){
        throw new UnsupportedOperationException("UiAddDamage not supported");
    }
    default void dispatch(UiAmmoUpdate message){
        throw new UnsupportedOperationException("UiAmmoUpdate not supported");
    }
    default void dispatch(UiDisablePowerUpEvent message){
        throw new UnsupportedOperationException("UiDisablePowerUpEvent not supported");
    }
    default void dispatch(UiContextSwitchEnd message){
        throw new UnsupportedOperationException("UiContextSwitchEnd not supported");
    }
    default void dispatch(UiFinalFrenzy message){
        throw new UnsupportedOperationException("UiFinalFrenzy not supported");
    }
    default void dispatch(UiChooseAmmoToPay message){
        throw new UnsupportedOperationException("UiChooseAmmoToPay not supported");
    }
    default void dispatch(UiDiscardPowerUps message){
        throw new UnsupportedOperationException("UiDiscardPowerUps not supported");
    }
    default void dispatch(UiReloading message){
        throw new UnsupportedOperationException("UiReloading not supported");
    }
    default void dispatch(UiStopReloading message){
        throw new UnsupportedOperationException("UiStopReloading not supported");
    }
}
