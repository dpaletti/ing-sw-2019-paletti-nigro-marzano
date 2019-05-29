package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class StartFirstTurnEvent extends MVEvent {
    private String firstPowerUpName;
    private String firstPowerUpColour;
    private String secondPowerUpName;
    private String secondPowerUpColour;
    public StartFirstTurnEvent(String destination,
                               String firstPowerUpColour,
                               String firstPowerUpName,
                               String secondPowerUpColour,
                               String secondPowerUpName){
        super(destination);
        this.firstPowerUpColour=firstPowerUpColour;
        this.firstPowerUpName=firstPowerUpName;
        this.secondPowerUpColour=secondPowerUpColour;
        this.secondPowerUpName=secondPowerUpName;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getFirstPowerUpColour() {
        return firstPowerUpColour;
    }

    public String getFirstPowerUpName() {
        return firstPowerUpName;
    }

    public String getSecondPowerUpColour() {
        return secondPowerUpColour;
    }

    public String getSecondPowerUpName() {
        return secondPowerUpName;
    }

}
