package it.polimi.se2019.client.view;

import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.client.view.ui_events.*;
import it.polimi.se2019.commons.vc_events.ChosenEffectEvent;
import it.polimi.se2019.commons.vc_events.ChosenWeaponEvent;
import it.polimi.se2019.commons.vc_events.DiscardedWeaponEvent;
import it.polimi.se2019.commons.vc_events.ReloadEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiControllerWeapon extends GuiController {

    @FXML
    private ImageView weaponLeft;


    @FXML
    private ImageView weaponMiddle;


    @FXML
    private ImageView weaponRight;

    private String left;
    private String middle;
    private String right;

    private Scene scene;

    private List<String> spentAmmos = new ArrayList<>();
    private String reloading;
    private List<String> reloadableWeapons = new ArrayList<>();
    private List<String> reloadedWeapons = new ArrayList<>();

    private String weaponInUse;

    @Override
    public void dispatch(UiTurnEnd message) {
        removeHandlers(weaponLeft);
        removeHandlers(weaponMiddle);
        removeHandlers(weaponRight);
    }

    @Override
    public void dispatch(UiShowFourth message) {
        handleDiscard(weaponRight, right);
        handleDiscard(weaponLeft, left);
        handleDiscard(weaponMiddle, middle);
    }

    private void handleDiscard(ImageView weapon, String toDiscard){
        weapon.setOnMouseClicked((MouseEvent event) -> {
            ViewGUI.getInstance().send(new DiscardedWeaponEvent(ViewGUI.getInstance().getUsername(), toDiscard));
            ((ImageView) event.getSource()).setOnMouseClicked(null);
        });
        weapon.setOnMouseEntered(clickable(scene));
        weapon.setOnMouseExited(notClickable(scene));
    }


    @Override
    public void dispatch(UiPutWeapon message) {
        if (scene == null)
            scene = weaponLeft.getScene();
        try {
            if (left == null) {
                removeHandlers(weaponLeft);
                weaponLeft.setImage(new Image(
                        Paths.get("files/assets/cards/" + message.getWeapon() + ".png").toUri().toURL().toString()

                ));
                left = message.getWeapon();
            }
            else if (middle == null) {
                removeHandlers(weaponMiddle);
                weaponMiddle.setImage(new Image(
                        Paths.get("files/assets/cards/" + message.getWeapon() + ".png").toUri().toURL().toString()

                ));
                middle = message.getWeapon();
            }
            else if (right == null) {
                removeHandlers(weaponRight);
                weaponRight.setImage(new Image(
                        Paths.get("files/assets/cards/" + message.getWeapon() + ".png").toUri().toURL().toString()

                ));
                right = message.getWeapon();
            } else {
                ViewGUI.getInstance().send(new UiShowFourth(message.getWeapon(), true));
                weaponLeft.setOnMouseEntered(clickable(scene));
                weaponLeft.setOnMouseExited(notClickable(scene));
                weaponLeft.setOnMouseClicked(handleDiscard(left));

                weaponRight.setOnMouseEntered(clickable(scene));
                weaponRight.setOnMouseExited(notClickable(scene));
                weaponRight.setOnMouseClicked(handleDiscard(right));

                weaponMiddle.setOnMouseEntered(clickable(scene));
                weaponMiddle.setOnMouseExited(notClickable(scene));
                weaponMiddle.setOnMouseClicked(handleDiscard(middle));
            }
        } catch (MalformedURLException e) {
            Log.severe("Could not retrieve weapon asset for: " + message.getWeapon());
        }
    }

    private EventHandler<MouseEvent> handleDiscard(String weaponName){
        return (MouseEvent event) -> {
            ViewGUI.getInstance().send(new DiscardedWeaponEvent(ViewGUI.getInstance().getUsername(), weaponName));
            if(weaponName.equals(left))
                left = null;
            else if(weaponName.equals(middle))
                middle = null;
            else if(weaponName.equals(right))
                right = null;
            else
                throw new IllegalArgumentException("Could not find: " + weaponName + " while ");
            removeHandlers((ImageView) event.getSource());
            ViewGUI.getInstance().send(new UiShowFourthEnd());
        };
    }

    @Override
    public void dispatch(UiActivateWeapons message) {
        handleChoice(weaponRight, right);
        handleChoice(weaponLeft, left);
        handleChoice(weaponMiddle, middle);
    }

    private void handleChoice(ImageView weapon, String toSend){
        weapon.setOnMouseClicked((MouseEvent event) ->{
            weaponInUse = toSend;
            ViewGUI.getInstance().send(new ChosenWeaponEvent(ViewGUI.getInstance().getUsername(), toSend));
            ((ImageView) event.getSource()).setOnMouseClicked(null);
            ((ImageView) event.getSource()).setOnMouseEntered(null);
            ((ImageView) event.getSource()).setOnMouseExited(null);
        });

        weapon.setOnMouseEntered(clickable(scene));
        weapon.setOnMouseExited(clickable(scene));
    }

    @Override
    public void dispatch(UiActivateWeaponEffects message) {
        String position;
        if(message.getWeaponName().equals(left))
            position = "Left";
        else if (message.getWeaponName().equals(middle))
            position = "Middle";
        else if (message.getWeaponName().equals(right))
            position = "Right";
        else
            throw new IllegalArgumentException("Could not find: " + message.getWeaponName() + "while showing effects");

        ImageView effectSpot;
        for(String effect: message.getEffects().keySet()){
            if(message.getEffects().get(effect) == -1) {
                effectSpot = ((ImageView) scene.lookup("#" + "effectBase" + position));
            }
            else if(message.getEffects().get(effect) == 0) {
                effectSpot = (ImageView) scene.lookup("#" + "effectAlternate" + position);
            }
            else if(message.getEffects().get(effect) >= 1 && message.getEffects().get(effect) <= 2) {
                effectSpot = (ImageView) scene.lookup("#" + "effectOptional" + message.getEffects().get(effect) + position);
            }
            else
                throw new IllegalArgumentException("Could not highlight effect with: " + message.getEffects().get(effect) + "as position descriptor");
            highlight(effectSpot, message.getWeaponName(), effect);
        }
    }

    public void dispatch(UiPartialSkippedEvent message){
        List<String> positions = new ArrayList<>();
        List<String> types = new ArrayList<>();
        ImageView effectSpot;

        positions.add("Left");
        positions.add("Right");
        positions.add("Middle");

        types.add("Alternate");
        types.add("Optional1");
        types.add("Optional2");
        types.add("Base");

        for (String position: positions){
            for(String type: types){
                effectSpot = (ImageView) scene.lookup("#effect" + type + position);
                removeHandlers(effectSpot);
                effectSpot.setImage(null);
            }
        }
    }

    @Override
    public void dispatch(UiContextSwitch message) {
        try {
            List<ImageView> weaponSpot = new ArrayList<>();
            weaponSpot.add(weaponLeft);
            weaponSpot.add(weaponMiddle);
            weaponSpot.add(weaponRight);
            int i = 0;
            for (String w : ViewGUI.getInstance().getWeapons()) {

                weaponSpot.get(i).setImage(new Image(
                        Paths.get("files/assets/cards/" + w + ".png").toUri().toURL().toString()

                ));
                i++;
            }
            for (ImageView w: weaponSpot){
                for(Node n: w.getParent().getChildrenUnmodifiable()){
                    n.setDisable(true);
                }
            }
        }catch (MalformedURLException e){
            Log.severe("Could not retrieve weapon during context switch");
        }
    }

    @Override
    public void dispatch(UiContextSwitchEnd message) {
        try {
            List<ImageView> weaponSpot = new ArrayList<>();
            weaponSpot.add(weaponLeft);
            weaponSpot.add(weaponMiddle);
            weaponSpot.add(weaponRight);
            int i = 0;
            for (String w : ViewGUI.getInstance().getWeapons()) {

                weaponSpot.get(i).setImage(new Image(
                        Paths.get("files/assets/cards/" + w + ".png").toUri().toURL().toString()

                ));
                i++;
            }
            for (ImageView w : weaponSpot) {
                for (Node n : w.getParent().getChildrenUnmodifiable()) {
                    n.setDisable(false);
                }
            }
        }catch (MalformedURLException e){
            Log.severe("Could not retrieve weapon while ending ContextSwitching");
        }

    }

    @Override
    public void dispatch(UiReloading message) {
        try {
            HashMap<String, ArrayList<String>> priceMap = message.getPriceMap();
            ImageView storingImage;

            for (String weapon : priceMap.keySet()) {
                if (weapon.equals(left)) {
                    storingImage = weaponLeft;
                    reloading = left;
                } else if (weapon.equals(middle)) {
                    storingImage = weaponMiddle;
                    reloading = middle;
                } else if (weapon.equals(right)) {
                    storingImage = weaponRight;
                    reloading = right;
                } else
                    throw new IllegalArgumentException("Could not find: " + weapon + " among currently held weapons while reloading");

                storingImage.setImage(new Image(Paths.get("files/assets/cards/" + reloading + ".png").toUri().toURL().toString()));
                storingImage.setOnMouseEntered(clickable(scene));
                storingImage.setOnMouseExited(notClickable(scene));
                storingImage.setOnMouseClicked(handleReload(priceMap, weapon));

            }
        }catch (MalformedURLException e){
            Log.severe("Could not get URL for: " + reloading + "while turning for reloading");
        }
    }

    @Override
    public void dispatch(UiStopReloading message) {
        removeHandlers(weaponLeft);
        removeHandlers(weaponMiddle);
        removeHandlers(weaponRight);


        ViewGUI.getInstance().send(new ReloadEvent(ViewGUI.getInstance().getUsername(), reloadedWeapons));

        reloadableWeapons = new ArrayList<>();
        spentAmmos = new ArrayList<>();
        reloadedWeapons = new ArrayList<>();
    }

    @Override
    public void dispatch(UiWeaponEnd message) {
        try {
            ImageView storage;
            if (weaponInUse.equals(left))
                storage = weaponLeft;
            else if (weaponInUse.equals(middle))
                storage = weaponMiddle;
            else if (weaponInUse.equals(right))
                storage = weaponRight;
            else
                throw new IllegalArgumentException("Could not find weapon in use: " + weaponInUse + " while turning");
            storage.setImage(new Image(Paths.get("files/assets/cards/weapon_back.png").toUri().toURL().toString()));
        }catch (MalformedURLException e){
            Log.severe("Could not retrieve URL while turning");
        }


    }

    private EventHandler<MouseEvent> handleReload(HashMap<String, ArrayList<String>> priceMap, String weapon){
        return (MouseEvent event) ->{
            ImageView localStoring;
            spentAmmos.addAll(getWeaponPrice(priceMap, reloading));
            reloadedWeapons.add(reloading);
            List<String> reloadables = reloadableWeapons(priceMap);
            reloadables.removeAll(reloadedWeapons);
            reloadableWeapons = reloadables;
            if(reloadableWeapons.isEmpty()){
                ViewGUI.getInstance().send(new UiStopReloading());
                return;
            }
            for(String w: priceMap.keySet()){
                if(!reloadableWeapons.contains(w)){
                    if (weapon.equals(left))
                        localStoring = weaponLeft;
                    else if(weapon.equals(middle))
                        localStoring = weaponMiddle;
                    else if(weapon.equals(right))
                        localStoring = weaponRight;
                    else
                        throw new IllegalArgumentException("Could not find weapon: " + weapon + "while updating reloading");
                    localStoring.setOnMouseClicked(null);
                    localStoring.setOnMouseExited(null);
                    localStoring.setOnMouseEntered(null);
                }
            }

        };
    }

    private List<String> reloadableWeapons(HashMap<String, ArrayList<String>> priceMap){
        List<String> totalAmmos = ViewGUI.getInstance().getHeadPlayerAmmos();
        List<String> reloadables = new ArrayList<>();

        for(String heldAmmo: totalAmmos){
            for(String spentAmmo: spentAmmos){
                if(heldAmmo.equals(spentAmmo))
                    totalAmmos.remove(heldAmmo);
            }
        }

        for(String weapon: priceMap.keySet()){
            if(isAffordable(priceMap.get(weapon), totalAmmos))
                reloadables.add(weapon);
        }
        return reloadables;
    }

    private boolean isAffordable(List<String> price, List<String> budget){
        boolean isPayed = false;
        for(String priceAmmo: price){
            for(String budgetAmmo: budget){
                if(priceAmmo.equals(budgetAmmo)) {
                    isPayed = true;
                    break;
                }
            }
            if(isPayed)
                budget.remove(priceAmmo);
            else
                return false;
            isPayed = false;
        }
        return true;
    }

    private ArrayList<String> getWeaponPrice(HashMap<String, ArrayList<String>> priceMap, String toFind){
        for(String weapon: priceMap.keySet()){
            if(weapon.equals(toFind))
                return priceMap.get(weapon);
        }
        throw new IllegalArgumentException("Could not find weapon: " + toFind + "in price map while reloading");
    }


    private void highlight(ImageView weaponEffect, String weaponName, String effectName){
        try {
            weaponEffect.setImage(new Image(
                    Paths.get("files/assets/rectangle_black.png").toUri().toURL().toString()
            ));
            weaponEffect.setOnMouseEntered(clickable(scene));
            weaponEffect.setOnMouseExited(notClickable(scene));
            weaponEffect.setOnMouseClicked((MouseEvent event) -> {
                ViewGUI.getInstance().send(new ChosenEffectEvent(ViewGUI.getInstance().getUsername(), effectName, weaponName));
                removeHandlers(weaponEffect);
                weaponEffect.setImage(null);
            });
        }catch (MalformedURLException e){
            Log.severe("Could not retrieve rectangle for highlighting weapon effects");
        }
    }

}
