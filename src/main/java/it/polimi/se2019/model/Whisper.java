package it.polimi.se2019.model;

import java.util.List;
import java.util.Map;

public class Whisper extends Weapon {

    @Override
    public GraphNode<Pair<Map<Player, List<Action>>, List<Ammo>>> effect(Player player) {
        return null;
    }
}
