package it.polimi.se2019.utility;

import java.io.Serializable;

public class Point implements Serializable {
    private int x;
    private int y;

    public Point (int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    //TODO make it immutable and delete setters
    //instantiate new point instead of setting

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDistance (Point centre){
        return (Math.abs((centre.getX()-x)+(centre.getY()-y)));
    }
}
