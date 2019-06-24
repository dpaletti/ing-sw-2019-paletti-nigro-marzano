package it.polimi.se2019.utility;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Targetable;

public enum ActionType {

    MOVE{
        @Override   //shooter is player to be moved, target is the Tile
        public void apply(Targetable target, Player shooter, Action action) {
            shooter.run(target.getPosition(), 1);
        }
    }, DAMAGE{
        @Override
        public void apply(Targetable target, Player shooter, Action action) {
            shooter.shootPeople((Player) target, action.getValue());
        }
    },  MARK{
        @Override
        public void apply(Targetable target, Player shooter, Action action) {
            shooter.markPeople((Player) target, action.getValue());
        }
    };

    public abstract void apply(Targetable target, Player shooter, Action action);
}
