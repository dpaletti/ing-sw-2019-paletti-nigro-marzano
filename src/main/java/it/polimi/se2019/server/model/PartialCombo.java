package it.polimi.se2019.server.model;

import it.polimi.se2019.server.model.Card;
import it.polimi.se2019.server.model.Game;
import it.polimi.se2019.server.model.Grabbable;
import it.polimi.se2019.commons.mv_events.AllowedWeaponsEvent;
import it.polimi.se2019.commons.mv_events.GrabbablesEvent;

public enum  PartialCombo {
    SHOOT {
        @Override
        public void use(Game game, String username) {
            game.send(new AllowedWeaponsEvent(username, Card.cardStringify(Card.cardToCard(game.userToPlayer(username).getLoadedWeapons()))));
        }
    },
    GRAB {
        @Override
        public void use(Game game, String username) {
            game.send(new GrabbablesEvent(username, Grabbable.grabbableStringify(Grabbable.grabbableToCard(game.userToPlayer(username).getWeapons()))));
        }
    },
    MOVE {
        @Override
        public void use(Game game, String username) {
            game.allowedMovements(username, username, 1);
        }
    },
    RELOAD {
        @Override
        public void use(Game game, String username) {
            game.unloadedWeapons(username);
        }
    };

    public abstract void use(Game game, String username);


}