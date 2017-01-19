package org.tiqwab.puyopuyo.checker;

import org.tiqwab.puyopuyo.Puyo;
import org.tiqwab.puyopuyo.scanner.Point;

import java.util.Arrays;

/**
 * Created by nm on 1/18/17.
 *   +
 *   +
 * + +
 */
public class L4Checker extends BlockCheckerBase {

    @Override
    public Point[] targetPoints(Point[] points) {
        return new Point[]{ points[1], points[3], points[4], points[5] };
    }

}
