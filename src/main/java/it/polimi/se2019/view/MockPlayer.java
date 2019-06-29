package it.polimi.se2019.view;

import it.polimi.se2019.utility.Point;

import java.util.ArrayList;
import java.util.List;

public class MockPlayer {
    private String username;
    private String playerColor; //colour, all lower case
    private List<String> hp = new ArrayList<>();
    private List<String> mark = new ArrayList<>();
    private List<String> ammos = new ArrayList<>();
    private List<String> weapons = new ArrayList<>();

    private Point position = new Point(-1, -1);

    public MockPlayer(String username, String colour){
        this.username = username;
        this.playerColor = colour;
    }

    public void setWeapons(List<String> weapons) {
        this.weapons = weapons;
    }


    public List<String> getWeapons() {
        return weapons;
    }

    public List<String> getAmmos() {
        return ammos;
    }

    public void setAmmos(List<String> ammos) {
        this.ammos = ammos;
    }

    public Point getPosition() {
        return position;
    }

    public synchronized void setPosition(Point position) {
        this.position = position;
    }

    public String getUsername() {
        return username;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setHp(List<String> hp) {
        this.hp = hp;
    }

    public void setMark(List<String> mark) {
        this.mark = mark;
    }

    public List<String> getHp() {
        return new ArrayList<>(hp);
    }

    public List<String> getMark() {
        return new ArrayList<>(mark);
    }
}
