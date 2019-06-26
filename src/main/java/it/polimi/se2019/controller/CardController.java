package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Targetable;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvent;

import java.util.HashSet;
import java.util.Set;

public class CardController extends Controller {

    public CardController(Game model, Server server, int roomNumber) {
        super(model, server, roomNumber);
    }

    public CardController (){
    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //ignore events that this controller does not support
            Log.fine("CardController ignored " + JsonHandler.serialize(message));
        }
    }

    protected Set<Targetable> intersect (Set<Targetable> first, Set<Targetable> second){
        Set<Targetable> finalSet= new HashSet<>();
        for (Targetable t: first){
            if (second.contains(t))
                finalSet.add(t);
        }
        return finalSet;
    }
}
