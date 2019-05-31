package it.polimi.se2019.model;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class PossibleEffectsEvent extends MVEvent {
    private String weaponName;
    private String colour;
    private String type;   //noOptions, oneOption, twoOptions, alternateMod

    public PossibleEffectsEvent(String destination, String weaponName, String colour, String type){
        super (destination);
        this.colour=colour;
        this.type=type;
        this.weaponName=weaponName;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getColour() {
        return colour;
    }

    public String getType() {
        return type;
    }

    public String getWeaponName() {
        return weaponName;
    }
}
