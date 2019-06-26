package it.polimi.se2019.utility;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Targetable;
import it.polimi.se2019.model.mv_events.AllowedMovementsEvent;

public enum ActionType {

    //TODO: define radius
    MOVE{
        @Override   //shooter is player to be moved, target is the Tile
        public void apply(Player target, Player shooter, Action action, Game game) {
           game.allowedMovements(game.playerToUser(shooter), game.playerToUser(target), 1);
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
