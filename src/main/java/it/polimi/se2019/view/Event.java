package it.polimi.se2019.view;

import java.io.Serializable;

public abstract class Event implements Serializable {
    //TODO event restructuring
    //create a decorator class that decorates event
    //event will come undecorated from the view and (maybe from the model too)
    //virtual view upon receiving this event will decorate it making it usable to view or model
    //this is because client should not see the controller while now it does.
}
