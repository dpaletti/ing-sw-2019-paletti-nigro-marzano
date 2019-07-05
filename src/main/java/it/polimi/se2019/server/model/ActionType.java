package it.polimi.se2019.server.model;

/**
 * This enum defines the type of actions that can be done while using a weapon or a power up.
 * Each of them overrides an apply method that defines the damage caused by them.
 */

public enum ActionType {

    MOVE{
        @Override   //shooter is player to be moved, target is the Tile
        public void apply(Player target, Player shooter, Action action, Game game) {
           game.allowedMovements(game.playerToUser(shooter), game.playerToUser(target), action.getValue());
        }
    }, DAMAGE{
        @Override
        public void apply(Player target, Player shooter, Action action, Game game) {
            shooter.shootPeople(target, action.getValue());
        }
    },  MARK{
        @Override
        public void apply(Player target, Player shooter, Action action, Game game) {
            shooter.markPeople(target, action.getValue());
        }
    };

    public abstract void apply(Player target, Player shooter, Action action, Game game);


}
