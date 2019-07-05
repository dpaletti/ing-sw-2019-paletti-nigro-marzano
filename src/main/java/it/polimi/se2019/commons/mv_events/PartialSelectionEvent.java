package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This event notifies the user of the target set, whether this effect can be skipped and whether the card is a weapon or a power up.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class PartialSelectionEvent extends MVEvent {
    private ArrayList<String> targetPlayers;
    private ArrayList<Point> targetTiles;
    private boolean isSkippable;
    private boolean isWeapon;

    public PartialSelectionEvent(String destination, List<String> targetPlayers, boolean isSkippable,boolean isWeapon) {
        super(destination);
        this.targetPlayers = new ArrayList<>(targetPlayers);
        this.isSkippable = isSkippable;
        this.isWeapon= isWeapon;
    }

    public PartialSelectionEvent(List<Point> targetTiles, String destination, boolean isSkippable,boolean isWeapon) {
        super(destination);
        this.targetTiles = new ArrayList<>(targetTiles);
        this.isSkippable = isSkippable;
        this.isWeapon=isWeapon;
    }

    public List<String> getTargetPlayers() {
        return targetPlayers;
    }

    public List<Point> getTargetTiles() {
        return targetTiles;
    }

    public boolean isSkippable() {
        return isSkippable;
    }

    public boolean isWeapon() {
        return isWeapon;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
