package it.polimi.se2019.view;

import it.polimi.se2019.utility.EventDispatcher;
import it.polimi.se2019.view.gui_events.*;

public interface GuiDispatcher extends EventDispatcher {
    default void dispatch(GuiAddPlayer message){
        throw new UnsupportedOperationException("GuiAddPlayer Unsupported");
    }
    default void dispatch(GuiTimerStart message){
        throw new UnsupportedOperationException("GuiTimerStart Unsupported");
    }
    default void dispatch(GuiTimerStop message){
        throw new UnsupportedOperationException("GuiTimerStop Unsupported");
    }
    default void dispatch(GuiTimerTick message){
        throw new UnsupportedOperationException("GuiTimerTick Unsupported");
    }
    default void dispatch(GuiRemovePlayer message){
        throw new UnsupportedOperationException("GuiRemovePlayer Unsupported");
    }
    default void dispatch(GuiCloseMatchMaking message){
        throw new UnsupportedOperationException("GuiCloseMatchMaking Unsupported");
    }
    default void dispatch(GuiInitialize message){
        throw new UnsupportedOperationException("GuiInitialized Unsupported");
    }
    default void dispatch(GuiRegistrationEvent message){
        throw new UnsupportedOperationException("GuiRegistrationEvent unsupported");
    }
    default void dispatch(GuiHighlightTileEvent message){
        throw new UnsupportedOperationException("GuiHighlightTileEvent unsupported");
    }
    default void dispatch(GuiMoveFigure message){
        throw new UnsupportedOperationException("GuiMoveFigureEvent unsupported");
    }

}
