package it.polimi.se2019.controller;

import it.polimi.se2019.model.Adrenaline;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.Event;
import it.polimi.se2019.view.View;

public class Controller implements Observer<Event> {
    private Adrenaline model;
    private View view;

    @Override
    public void update(Event message) {

    }
}