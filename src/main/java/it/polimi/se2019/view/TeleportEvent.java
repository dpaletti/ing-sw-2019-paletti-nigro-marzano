package it.polimi.se2019.view;

import it.polimi.se2019.network.Connection;

public class TeleportEvent extends MVEvent {
   private int x;
   private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TeleportEvent (int x, int y){
        this.x=x;
        this.y=y;

    }
}
