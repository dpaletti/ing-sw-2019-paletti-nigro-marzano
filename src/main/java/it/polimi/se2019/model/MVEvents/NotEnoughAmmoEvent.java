package it.polimi.se2019.model.MVEvents;

import it.polimi.se2019.view.MVEvent;

public class NotEnoughAmmoEvent extends MVEvent {

    public NotEnoughAmmoEvent(){
        super();
    }

    public NotEnoughAmmoEvent(String destination){
        super(destination);
    }
}
