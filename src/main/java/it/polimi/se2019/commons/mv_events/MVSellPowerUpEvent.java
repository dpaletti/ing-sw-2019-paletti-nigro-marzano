package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//sent when ammos are not enough to pay for price of an action but powerups could cover that price

public class MVSellPowerUpEvent extends MVEvent {
    private ArrayList<String> powerUpsToSell;
    private HashMap<String, Integer> coloursToMissing;  //maps colour of missing ammo to its colour

    public MVSellPowerUpEvent(String destination, List<String> powerUpsToSell, Map<String, Integer> coloursToMissing) {
        super(destination);
        this.powerUpsToSell = new ArrayList<>(powerUpsToSell);
        this.coloursToMissing = new HashMap<>(coloursToMissing);
    }

    public List<String> getPowerUpsToSell() {
        return powerUpsToSell;
    }

    public Map<String, Integer> getColoursToMissing() {
        return coloursToMissing;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
