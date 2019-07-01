package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.mv_events.GrabbedWeaponEvent;
import it.polimi.se2019.commons.mv_events.MVSellPowerUpEvent;

import java.util.HashMap;
import java.util.Map;

public class Weapon extends Card implements Grabbable, Drawable, Jsonable{
    private boolean loaded = true;

    public boolean getLoaded() {
        return loaded;
    }

    public Weapon (String path){
            super(path);
    }

    public Weapon(Weapon weapon){
        super(weapon);
        this.loaded=weapon.loaded;
    }

    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

    //check number of weapons in hand
    //if < 3, pay cost and add weapon to list
    //else send choice event to game

    @Override
    public void grab(Player player, String grabbed, Game game) {
        int index =-1;
        for (Grabbable w : player.getFigure().getTile().getGrabbables()){
            if (w.getName().equalsIgnoreCase(grabbed)) {
               index = player.getFigure().getTile().getGrabbables().indexOf(w);
               break;
            }
        }
        if (index == -1)
            throw new UnsupportedOperationException(grabbed + "\t is not in the current weapon spot and cannot be grabbed");
        if (player.getWeapons().size() < 3){
            if(player.missingAmmos(((Weapon)player.getFigure().getTile().grabbables.get(index)).price).isEmpty()) {  //price could be and was paid
                player.addWeapon((Weapon) player.getFigure().getTile().grabbables.get(index));
                game.send(new GrabbedWeaponEvent("*", grabbed, game.playerToUser(player)));
                player.getFigure().getTile().removeGrabbed(grabbed);
                game.addEmptySpawnTile(player.getPosition());
            }
            else{
                if (!player.powerUpsToPay(((Weapon)player.getFigure().getTile().grabbables.get(index)).price).isEmpty()) {
                    Map<String, Integer> colourToMissing = new HashMap<>();
                    for (Ammo a : (player.missingAmmos(((Weapon)player.getFigure().getTile().grabbables.get(index)).price))) {
                        if (colourToMissing.containsKey(a.getColour().name()))
                            colourToMissing.put(a.getColour().name(), colourToMissing.get(a.getColour().name()) + 1);
                        else
                            colourToMissing.put(a.getColour().name(), 1);
                    }
                    game.send(new MVSellPowerUpEvent(game.playerToUser(player),
                            player.powerUpsToPay(((Weapon)player.getFigure().getTile().grabbables.get(index)).price),
                            colourToMissing));
                }
            }
        }
    }


}
