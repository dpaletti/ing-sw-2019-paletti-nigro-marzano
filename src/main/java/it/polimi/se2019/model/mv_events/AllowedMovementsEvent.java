package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.model.Point;
import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class AllowedMovementsEvent extends MVEvent {
    ArrayList<Point> allowedPositions;

    public AllowedMovementsEvent(String destination, List<Point> allowedPositions){
        super(destination);
        this.allowedPositions= new ArrayList<>(allowedPositions);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
