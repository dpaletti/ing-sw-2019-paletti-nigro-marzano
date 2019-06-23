package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.AllowedWeaponsEvent;
import it.polimi.se2019.model.mv_events.GrabbablesEvent;

public enum  PartialCombo {
    SHOOT {
        @Override
        public void use(Game game, String username) {
            game.send(new AllowedWeaponsEvent(username, Card.stringify(Card.toCard(game.userToPlayer(username).getWeapons()))));
        }
    },
    GRAB{
        @Override
        public void use(Game game, String username) {
            game.send(new GrabbablesEvent(username, Grabbable.stringify(Grabbable.toCard(game.userToPlayer(username).getWeapons()))));
        }
    },
    MOVE {
        @Override
        public void use(Game game, String username) {
            game.allowedMovements(username, 1);
        }
    },
    RELOAD{
        @Override
        public void use(Game game, String username) {
            game.unloadedWeapons(username);
        }
    };

    public abstract void use (Game game, String username);
}
