package it.polimi.se2019.model;

import java.util.HashSet;
import java.util.Set;

public class LootCard implements Grabbable, Drawable,Jsonable{
    private String name;
    private Set<Ammo> ammo= new HashSet<>();

    public LootCard (LootCard lootCard){
        this.name= lootCard.name;
        this.ammo= new HashSet<>(lootCard.ammo);
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

    public Set<Ammo> getAmmo() {
        return ammo;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Jsonable copy() {
        return new LootCard(this);
    }

    @Override
    public void grab(Player player, String grabbed) {
        if (name.equalsIgnoreCase(grabbed))
            throw new UnsupportedOperationException("Selected grabbable is not on current tile and cannot be grabbed");
        for (char c: name.toCharArray()){
            if(c != 'P')
                addAmmo(c, player);
        }

        if (name.contains("P"))
            player.drawPowerUp();
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
