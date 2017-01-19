package org.tiqwab.puyopuyo.checker;

import org.tiqwab.puyopuyo.Board;
import org.tiqwab.puyopuyo.Puyo;
import org.tiqwab.puyopuyo.scanner.Point;

/**
 * Created by nm on 1/18/17.
 */
public interface BlockChecker {
    public void check(Point[] targetPoints, Board board);
    public Point[] targetPoints(Point[] points);
}
