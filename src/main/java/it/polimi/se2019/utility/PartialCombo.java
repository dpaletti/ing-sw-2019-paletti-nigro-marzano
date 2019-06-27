package it.polimi.se2019.utility;

import it.polimi.se2019.model.Card;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Grabbable;
import it.polimi.se2019.model.mv_events.AllowedWeaponsEvent;
import it.polimi.se2019.model.mv_events.GrabbablesEvent;

public enum  PartialCombo {
    SHOOT {
        @Override
        public void use(Game game, String username) {
            game.send(new AllowedWeaponsEvent(username, Card.cardStringify(Card.cardToCard(game.userToPlayer(username).getWeapons()))));
        }
    },
    GRAB{
        @Override
        public void use(Game game, String username) {
            game.send(new GrabbablesEvent(username, Grabbable.grabbableStringify(Grabbable.grabbableToCard(game.userToPlayer(username).getWeapons()))));
        }
    },
    MOVE {
        @Override
        public void use(Game game, String username) {
            game.allowedMovements(username, "",  1);
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
