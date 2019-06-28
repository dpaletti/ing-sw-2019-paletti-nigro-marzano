package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

//sent when ammos are not enough to pay for price of an action but powerups could cover that price

public class MVSellPowerUpEvent extends MVEvent {
    private ArrayList<String> powerUpsToSell;
    private int missingPowerUps;

    public MVSellPowerUpEvent(String destination, List<String> powerUpsToSell, int missingPowerUps) {
        super(destination);
        this.powerUpsToSell = new ArrayList<>(powerUpsToSell);
        this.missingPowerUps = missingPowerUps;
    }

    public List<String> getPowerUpsToSell() {
        return powerUpsToSell;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
