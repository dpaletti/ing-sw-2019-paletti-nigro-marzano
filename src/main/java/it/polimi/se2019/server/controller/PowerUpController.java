package it.polimi.se2019.server.controller;

import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.mv_events.PossibleEffectsEvent;
import it.polimi.se2019.commons.utility.JsonHandler;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.vc_events.ChosenEffectPowerUpEvent;
import it.polimi.se2019.commons.vc_events.PowerUpUsageEvent;
import it.polimi.se2019.commons.vc_events.VCChooseAmmoToPayEvent;
import it.polimi.se2019.server.model.Game;
import it.polimi.se2019.server.model.GraphNode;
import it.polimi.se2019.server.model.GraphWeaponEffect;
import it.polimi.se2019.server.network.Server;

import java.util.ArrayList;
import java.util.List;

public class PowerUpController extends CardController {
    private String currentPowerUp;

    public PowerUpController(Game model, Server server, int roomNumber) {
        super(model, server, roomNumber);
    }

    public PowerUpController(Game model) {
        super(model);
    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(this);
        } catch (UnsupportedOperationException e) {
            //ignore events that this controller does not support
            Log.fine("PowerUpController ignored " + JsonHandler.serialize(message));
        }
    }

    @Override
    public void dispatch(PowerUpUsageEvent message) {
        currentPowerUp = message.getUsedPowerUp();
        layersVisited = layersVisited + 1;
        List<GraphWeaponEffect> list = new ArrayList<>();
        for (GraphNode<GraphWeaponEffect> g: current.getDefinition().getListLayer(layersVisited))
            list.add(g.getKey());
        if (!list.isEmpty()) {
            PossibleEffectsEvent event = new PossibleEffectsEvent(model.playerToUser(currentPlayer), current.getName(), false);
            for (GraphWeaponEffect w: list)
                event.addEffect(w.getName(), w.getEffectType());
            model.send(event);
        }
        else
            endUsage();
    }

    @Override
    public void dispatch(VCChooseAmmoToPayEvent message) {

    }

    @Override
    public void dispatch(ChosenEffectPowerUpEvent message) {
        GraphWeaponEffect weaponEffect = null;

        for (GraphNode<GraphWeaponEffect> w: model.nameToWeapon(message.getPowerUp()).getDefinition()) {
            if(w.getKey().getName().equals(message.getEffectName())) {
                weaponEffect = w.getKey();
                break;
            }
        }
        if(weaponEffect == null)
            throw new NullPointerException("Could not find " + message.getEffectName() + " in " + message.getPowerUp());
        layersVisitedPartial = 1;
        currentLayer= weaponEffect.getEffectGraph().getListLayer(layersVisitedPartial);
        /*for(GraphNode<PartialWeaponEffect> p: currentLayer)
            model.addToSelection(message.getSource(),
                    p.getKey().getActions(),
                    generateTargetSet(p.getKey(),
                            model.userToPlayer(message.getSource())));

        model.sendPossibleTargets();*/
    }
}