package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.mv_events.MVSelectionEvent;
import it.polimi.se2019.commons.utility.Point;

import java.util.List;
import java.util.Map;

/**
 * This interface gathers methods that tiles and players have in common as both can be considered as targets of weapons or power ups.
 */

public interface Targetable {

    /**
     * adds the hit players or tiles to the turn memory.
     * @param partialWeaponEffect the effect that was used to hit them.
     * @param hit the hit targetables.
     * @param turnMemory a reference to the turn memory.
     */
    void hit(String partialWeaponEffect, List<Targetable> hit, TurnMemory turnMemory);

    /**
     * retrieves hit users by a certain effect.
     * @param effects the effects the needed users were hit by.
     * @param turnMemory a reference to the turn memory.
     * @return returns the users hit by the desired effects.
     */
    List <Targetable> getByEffect (List<String> effects, TurnMemory turnMemory);
    List<Targetable> getAll();
    Point getPosition();
    Map<String, List<Targetable>> getHitTargets(TurnMemory turnMemory);
    void addToSelectionEvent(MVSelectionEvent event, List<Targetable> targets, List<Action> actions);
    List<Targetable> getPlayers();
}
