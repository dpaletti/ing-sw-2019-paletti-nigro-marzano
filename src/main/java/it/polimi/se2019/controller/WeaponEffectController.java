package it.polimi.se2019.controller;

import it.polimi.se2019.model.*;
import it.polimi.se2019.network.Server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeaponEffectController extends WeaponController{

    public WeaponEffectController(Server server, int roomNumber, Game model) {
        super(server, roomNumber, model);
    }

//TODO

    private PartialWeaponEffect selectNode(Targetable selectedTargets, Player player){
        Set<PartialWeaponEffect> selectedEffect= new HashSet<>();
        for (GraphNode<PartialWeaponEffect> g: currentLayer){
            if (generateTargetSet(g.getKey(), player).contains(selectedTargets))
                selectedEffect.add(g.getKey());
        }
        if (selectedEffect.isEmpty())
            throw new IllegalArgumentException("the selected targets are illegal");
        else if (selectedEffect.size()==1)
            return selectedEffect.iterator().next();
        else{
            List<ArrayList<String>> targetsToChoose = new ArrayList<>();
            boolean isTile= selectedEffect.iterator().next().getTargetSpecification().getTile();
            List<String> targetsByEffect = null;

            for (PartialWeaponEffect p: selectedEffect){
                targetsByEffect= new ArrayList<>();
                if (!isTile==p.getTargetSpecification().getTile())
                    throw new IllegalArgumentException("the specified targetset is illegal");
                if (isTile) {
                    for (Targetable t : generateTargetSet(p, player)) {
                        if (p.getTargetSpecification().getArea()) {
                            for (Figure f : model.getGameMap().getMap().get(t.getPosition()).getFigures()) {
                                targetsByEffect.add(model.colourToUser(f.getColour()));
                            }
                        } else {
                            for (Figure f : model.getGameMap().getMap().get(t.getPosition()).getFigures()) {
                                targetsByEffect.add(model.colourToUser(f.getColour()));
                                targetsToChoose.add(new ArrayList<>(targetsByEffect));
                            }

                        }
                    }
                }

                else{
                    List<String> targets= new ArrayList<>();
                    targets.add(model.playerToUser((Player)selectedTargets));
                    targetsToChoose.add(new ArrayList<>(targets));
                }
            }
            //model.sendPartialEffectConflict(model.playerToUser(player));
        }
        return null;
    }
}
