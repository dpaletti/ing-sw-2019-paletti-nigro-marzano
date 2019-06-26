package it.polimi.se2019.view;

import it.polimi.se2019.utility.Point;

import java.util.ArrayList;
import java.util.List;

public class MockPlayer {
    private String username;
    private String playerColor; //colour, all lower case
    private List<String> hp = new ArrayList<>();
    private List<String> mark = new ArrayList<>();

    private int value = 8; //how much the player is worth when dying (8, 6, 4, 2, 1, 1)
    private boolean secondOne = false;

    private String leftPowerUp = "none";
    private String middlePowerUp = "none";
    private String rightPowerUp = "none";

    private String leftWeapon = "none";
    private String middleWeapon = "none";
    private String rightWeapon = "none";
    private Point position = new Point(-1, -1);

    public MockPlayer(String username, String colour){
        this.username = username;
        this.playerColor = colour;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public String getUsername() {
        return username;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setHp(List<String> hp) {
        this.hp = hp;
    }

    public void setLeftPowerUp(String leftPowerUp) {
        this.leftPowerUp = leftPowerUp;
    }

    public void setLeftWeapon(String leftWeapon) {
        this.leftWeapon = leftWeapon;
    }

    public void setMark(List<String> mark) {
        this.mark = mark;
    }

    public void setMiddlePowerUp(String middlePowerUp) {
        this.middlePowerUp = middlePowerUp;
    }

    public void setMiddleWeapon(String middleWeapon) {
        this.middleWeapon = middleWeapon;
    }

    public void setRightPowerUp(String rightPowerUp) {
        this.rightPowerUp = rightPowerUp;
    }

    public void setRightWeapon(String rightWeapon) {
        this.rightWeapon = rightWeapon;
    }

    public List<String> getHp() {
        return new ArrayList<>(hp);
    }

    public List<String> getMark() {
        return new ArrayList<>(mark);
    }

    public void decreaseValue(){
        if(value == 8){
            value = 6;
        }
        else if(value == 6){
            value = 4;
        }
        else if(value == 4){
            value = 2;
        }
        else if(value == 2){
            value = 1;
        }
        else if(value == 1){
            secondOne = true;
        }
    }

    public int getValue() {
        return value;
    }

    public boolean isSecondOne() {
        return secondOne;
    }

    public String getLeftPowerUp() {
        return leftPowerUp;
    }

    public String getLeftWeapon() {
        return leftWeapon;
    }

    public String getMiddlePowerUp() {
        return middlePowerUp;
    }

    public String getMiddleWeapon() {
        return middleWeapon;
    }

    public String getRightPowerUp() {
        return rightPowerUp;
    }

    public String getRightWeapon() {
        return rightWeapon;
    }
}
