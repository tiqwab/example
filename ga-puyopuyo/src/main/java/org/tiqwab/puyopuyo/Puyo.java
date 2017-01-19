package org.tiqwab.puyopuyo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nm on 1/18/17.
 */
public class Puyo {

    private final int color;
    private final boolean willBeDeleted;

    public Puyo(int color) {
        this(color, false);
    }

    public Puyo(int color, boolean willBeDeleted) {
        this.color = color;
        this.willBeDeleted = willBeDeleted;
    }

    public Puyo(Puyo puyo) {
        this(puyo.color, puyo.willBeDeleted);
    }

    public static Puyo emptyPuyo() {
        return new Puyo(0);
    }

    public Puyo willDelete() {
        return new Puyo(this.color, true);
    }

    public boolean deleted() {
        return this.color == 0;
    }

    public int getColor() {
        return this.color;
    }

    public boolean willBeDeleted() {
        return this.willBeDeleted;
    }

    public static boolean eauqlAll(List<Puyo> puyos) {
        final int max = puyos.stream().map(p -> p.color).max(Integer::compareTo).orElseThrow(RuntimeException::new);
        final int min = puyos.stream().map(p -> p.color).min(Integer::compareTo).orElseThrow(RuntimeException::new);
        return max == min;
    }

    @Override
    public String toString() {
        return Integer.toString(this.color);
    }

    public String toStringVerbose() {
        return String.format("(%d, %s)", this.color, this.willBeDeleted ? "t" : "f");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Puyo puyo = (Puyo) o;

        return color == puyo.color;
    }

    @Override
    public int hashCode() {
        return color;
    }

}
