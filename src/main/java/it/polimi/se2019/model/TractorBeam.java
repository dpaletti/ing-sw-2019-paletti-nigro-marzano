package it.polimi.se2019.model;

import java.util.List;
import java.util.Map;

public class TractorBeam extends Weapon {
    @Override
    public TreeNode<Pair<Map<Player, List<Action>>, List<Ammo>>> effect(Player player) {
        return null;
    }
}
