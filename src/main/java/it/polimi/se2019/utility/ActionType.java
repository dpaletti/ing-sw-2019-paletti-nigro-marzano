package it.polimi.se2019.utility;

import it.polimi.se2019.model.Player;

public enum ActionType {

    MOVE{
        @Override   //where do I get direction?
        public void apply(Player target, Player shooter, Action action) {
            //target.run(, 1);
        }
    }, DAMAGE{
        @Override
        public void apply(Player target, Player shooter, Action action) {
            shooter.shootPeople(target, action.getValue());
        }
    },  MARK{
        @Override
        public void apply(Player target, Player shooter, Action action) {
            shooter.markPeople(target, action.getValue());
        }
    };

    public abstract void apply(Player target, Player shooter, Action action);
}
