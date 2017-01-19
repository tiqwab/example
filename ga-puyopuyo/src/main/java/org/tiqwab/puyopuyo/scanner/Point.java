package org.tiqwab.puyopuyo.scanner;

/**
 * Created by nm on 1/19/17.
 */
public class Point {

    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", this.x, this.y);
    }
}
