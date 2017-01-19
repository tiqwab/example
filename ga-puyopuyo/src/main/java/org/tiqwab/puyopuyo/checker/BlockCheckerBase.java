package org.tiqwab.puyopuyo.checker;

import org.tiqwab.puyopuyo.Board;
import org.tiqwab.puyopuyo.Puyo;
import org.tiqwab.puyopuyo.scanner.Point;

import java.util.Arrays;

/**
 * Created by nm on 1/19/17.
 */
public abstract class BlockCheckerBase implements BlockChecker {

    @Override
    public void check(Point[] points, Board board) {
        Point[] focused = targetPoints(points);
        board.checkPuyos(focused);
    }

}
