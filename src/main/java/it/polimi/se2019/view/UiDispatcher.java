package it.polimi.se2019.view;

import it.polimi.se2019.utility.EventDispatcher;
import it.polimi.se2019.view.gui_events.*;

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
    default void dispatch(UiRegistrationEvent message){
        throw new UnsupportedOperationException("UiRegistrationEvent unsupported");
    }
    default void dispatch(UiHighlightTileEvent message){
        throw new UnsupportedOperationException("UiHighlightTileEvent unsupported");
    }
    default void dispatch(UiMoveFigure message){
        throw new UnsupportedOperationException("GuiMoveFigureEvent unsupported");
    }

    default void dispatch(UiMatchSetup message){
        throw new UnsupportedOperationException("UiMatchSetup unsupported");
    }
}
