package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.mv_events.AllowedWeaponsEvent;
import it.polimi.se2019.commons.mv_events.GrabbablesEvent;

/**
 * This enum defines the atomic actions that can be used to build a combo {@link it.polimi.se2019.server.model.Combo} in any phase of the game and with any damage.
 */

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

    /**
     * Each element of the enum implements this method and notifies the current player communicating the possibilities their chosen partial combo gives them.
     * @param game This is the class that is observable of model-view Events communicates with the view.
     * @param username This is the currently playing user.
     */

    public abstract void use(Game game, String username);


}