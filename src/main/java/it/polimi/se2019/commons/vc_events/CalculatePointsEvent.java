package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

/**
 * Event sent when the match is over in order to allow the DeathController to calculate the points at the end of the match.
 * See {@link it.polimi.se2019.client.view.VCEvent}.
 */

public class CalculatePointsEvent extends VCEvent {

    public CalculatePointsEvent(String source) {
        super(source);
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
