package it.polimi.se2019.commons.utility;

import java.io.Serializable;
import java.util.Objects;

/**
 * Point class consists of a pair of integers indicating the spacial coordinates of a certain element.
 * It implements serializable in order to allow the usage of Point objects in the interaction through the network.
 */

public class Point implements Serializable {
    private int x;
    private int y;

    public Point (int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point (Point point){
        this.x = point.getX();
        this.y = point.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
