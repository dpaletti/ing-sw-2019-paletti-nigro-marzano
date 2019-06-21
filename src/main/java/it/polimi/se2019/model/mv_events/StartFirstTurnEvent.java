package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class StartFirstTurnEvent extends MVEvent {
    private String firstPowerUpName;
    private String secondPowerUpName;
    private boolean isFirstPlayer;
    public StartFirstTurnEvent(String destination,
                               String firstPowerUpName,
                               String secondPowerUpName,
                               boolean isFirstPlayer){
        super(destination);
        this.firstPowerUpName=firstPowerUpName;
        this.secondPowerUpName=secondPowerUpName;
        this.isFirstPlayer=isFirstPlayer;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getFirstPowerUpName() {
        return firstPowerUpName;
    }

    public String getSecondPowerUpName() {
        return secondPowerUpName;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }
}
