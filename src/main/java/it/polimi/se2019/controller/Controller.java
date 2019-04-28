package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.View;

public class Controller implements Observer<VCEvent> {
    private Game model;
    private View view;

    @Override
    public void update(VCEvent message) {
    }


}
