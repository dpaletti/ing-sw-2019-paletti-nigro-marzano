package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.mv_events.MVSelectionEvent;
import it.polimi.se2019.commons.utility.Point;

import java.util.List;
import java.util.Map;

public interface Targetable {

    void hit(String partialWeaponEffect, List<Targetable> hit, TurnMemory turnMemory);
    List <Targetable> getByEffect (List<String> effects, TurnMemory turnMemory);
    List<Targetable> getAll();
    Point getPosition();
    Map<String, List<Targetable>> getHitTargets(TurnMemory turnMemory);
    void addToSelectionEvent(MVSelectionEvent event, List<Targetable> targets, List<Action> actions);
    List<Targetable> getPlayers();
}
