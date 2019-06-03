package it.polimi.se2019.model;

import java.util.List;

public interface Targetable {
    void hit(String partialWeaponEffect, List<? extends Targetable> hit, TurnMemory turnMemory);
    List<? extends Targetable> getByEffect (List<String> effects, TurnMemory turnMemory); //NOSONAR
}
