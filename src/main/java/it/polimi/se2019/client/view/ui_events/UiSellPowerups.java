package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

import java.util.List;
import java.util.Map;

public class UiSellPowerups extends UiEvent {

    private List<String> powerUpsToSell;
    private Map<String, Integer> coloursToMissing;  //maps colour of missing ammo to its colour

    public UiSellPowerups(List<String> powerUpsToSell, Map<String, Integer> coloursToMissing) {
        this.powerUpsToSell = powerUpsToSell;
        this.coloursToMissing = coloursToMissing;
    }


    public List<String> getPowerUpsToSell() {
        return powerUpsToSell;
    }

    public Map<String, Integer> getColoursToMissing() {
        return coloursToMissing;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

}

