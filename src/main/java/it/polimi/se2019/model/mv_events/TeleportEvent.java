package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class TeleportEvent extends MVEvent {
   private int x;
   private int y;


   public TeleportEvent(int x, int y){
       super();
       this.x = x;
       this.y = y;
   }

    public TeleportEvent (String destination, int x, int y){
        super(destination);
        this.x=x;
        this.y=y;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
