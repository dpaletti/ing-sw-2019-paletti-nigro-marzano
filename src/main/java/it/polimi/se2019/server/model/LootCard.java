package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.mv_events.DrawnPowerUpEvent;
import it.polimi.se2019.commons.mv_events.GrabbedLootCardEvent;

import java.util.ArrayList;
import java.util.List;

public class LootCard implements Grabbable, Drawable,Jsonable{
    private String name;
    private List<Ammo> ammo= new ArrayList<>();

    public LootCard (LootCard lootCard){
        this.name= lootCard.name;
        this.ammo= new ArrayList<>(lootCard.ammo);
    }

    public LootCard (String name, AmmoColour firstAmmoColour, AmmoColour secondAmmoColour, AmmoColour thirdAmmoColour){
        this.name = name;
        ammo.add(new Ammo(firstAmmoColour));
        ammo.add(new Ammo(secondAmmoColour));
        ammo.add(new Ammo(thirdAmmoColour));

    }

    public LootCard (String name, AmmoColour firstAmmoColour, AmmoColour secondAmmoColour){
        this.name = name;
        ammo.add(new Ammo(firstAmmoColour));
        ammo.add(new Ammo(secondAmmoColour));
    }

    public List<Ammo> getAmmo() {
        return ammo;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void grab(Player player, String grabbed, Game game) {
        if (name.equalsIgnoreCase(grabbed)) {
            for (char c : name.toCharArray()) {
                if (c != 'P')
                    addAmmo(c, player);
            }

            if (name.contains("P")) {
                player.drawPowerUp();
                game.send (new DrawnPowerUpEvent(game.playerToUser(player),
                        player.getPowerUps().get(player.getPowerUps().size() - 1).name));
                game.usablePowerUps("onTurn", false, player);
            }
            player.getFigure().getTile().removeGrabbed(grabbed);
            game.addEmptyLootTile(player.getPosition());
            game.send(new GrabbedLootCardEvent("*", grabbed));
        }
    }

    private void addAmmo (char c, Player player){
        Ammo ammo;
        int counter = 0;
        switch (c){
            case 'Y':
                ammo = new Ammo(AmmoColour.YELLOW);
                break;
            case 'B':
                ammo = new Ammo(AmmoColour.BLUE);
                break;
            case'R':
                ammo = new Ammo(AmmoColour.RED);
                break;
            case 'P':
                return;
            default:
                throw new UnsupportedOperationException("Ammo with selected colour could not be found");
        }

        for (Ammo a: player.getAmmo()){
            if (a.getColour().equals(ammo.getColour()))
                counter++;
        }

        if (counter < 3)
            player.addAmmo(ammo);
    }

}
