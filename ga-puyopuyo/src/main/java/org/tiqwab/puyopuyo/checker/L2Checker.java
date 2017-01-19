package org.tiqwab.puyopuyo.checker;

import org.tiqwab.puyopuyo.Puyo;
import org.tiqwab.puyopuyo.scanner.Point;

import java.util.Arrays;

/**
 * Created by nm on 1/18/17.
 * + +
 *   +
 *   +
 */
public class L2Checker extends BlockCheckerBase {

    @Override
    public Point[] targetPoints(Point[] points) {
        return new Point[]{ points[0], points[1], points[3], points[5] };
    }

}
