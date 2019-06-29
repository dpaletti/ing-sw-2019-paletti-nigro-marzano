package it.polimi.se2019.view;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.ui_events.*;
import it.polimi.se2019.view.vc_events.ChosenEffectEvent;
import it.polimi.se2019.view.vc_events.ChosenWeaponEvent;
import it.polimi.se2019.view.vc_events.DiscardedWeaponEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        if (scene != null)
            scene = weaponLeft.getScene();
        try {
            if (weaponLeft.getImage() == null) {
                weaponLeft.setImage(new Image(
                        Paths.get("files/assets/cards/" + message.getWeapon() + ".png").toUri().toURL().toString()

                ));
                left = message.getWeapon();
            }
            if (weaponMiddle.getImage() == null) {
                weaponMiddle.setImage(new Image(
                        Paths.get("files/assets/cards/" + message.getWeapon() + ".png").toUri().toURL().toString()

                ));
                middle = message.getWeapon();
                if (weaponRight.getImage() == null) {
                    weaponRight.setImage(new Image(
                            Paths.get("files/assets/cards/" + message.getWeapon() + ".png").toUri().toURL().toString()

                    ));
                    right = message.getWeapon();
                }
            } else
                ViewGUI.getInstance().send(new UiShowFourth(message.getWeapon(), true));
        } catch (MalformedURLException e) {
            Log.severe("Could not retrieve weapon asset for: " + message.getWeapon());
        }
    }

    @Override
    public void dispatch(UiActivateWeapons message) {
        handleChoice(weaponRight, right);
        handleChoice(weaponLeft, left);
        handleChoice(weaponMiddle, middle);
    }

    private void handleChoice(ImageView weapon, String toSend){
        weapon.setOnMouseClicked((MouseEvent event) ->{
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
                effectSpot = ((ImageView) scene.lookup("#" + "effectBase" + position + ".png"));
            }
            else if(message.getEffects().get(effect) >= 0 && message.getEffects().get(effect) <= 2) {
                effectSpot = (ImageView) scene.lookup("#" + "effectAlternate" + message.getEffects().get(effect) + position + ".png");
            }
            else
                throw new IllegalArgumentException("Could not highlight effect with: " + message.getEffects().get(effect) + "as position descriptor");
            highlight(effectSpot, message.getWeaponName(), effect);
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
        
    }

    private void highlight(ImageView weaponEffect, String weaponName, String effectName){
        try {
            weaponEffect.setImage(new Image(
                    Paths.get("files/assets/rectangle_black.png").toUri().toURL().toString()
            ));
            weaponEffect.setOnMouseEntered(clickable(scene));
            weaponEffect.setOnMouseExited(notClickable(scene));
            weaponEffect.setOnMouseClicked((MouseEvent event) -> {
                ViewGUI.getInstance().send(new ChosenEffectEvent(ViewGUI.getInstance().getUsername(), weaponName, effectName));
                removeHandlers(weaponEffect);
            });
        }catch (MalformedURLException e){
            Log.severe("Could not retrieve rectangle for highlighting weapon effects");
        }
    }

}
