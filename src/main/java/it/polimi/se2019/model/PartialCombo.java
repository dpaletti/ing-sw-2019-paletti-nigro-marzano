package it.polimi.se2019.model;

public enum  PartialCombo {
    SHOOT {
        @Override
        public void use(Game game, String username) {
            game.allowedWeapons(username);
        }
    },
    GRAB{
        @Override
        public void use(Game game, String username) {
            game.grabbableCards(username);
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
