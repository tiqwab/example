package org.tiqwab.puyopuyo.checker;

import org.tiqwab.puyopuyo.scanner.Point;

/**
 * Created by nm on 1/18/17.
 * +
 * + + +
 */
public class Ld1Checker extends BlockCheckerBase {

    @Override
    public Point[] targetPoints(Point[] points) {
        return new Point[]{ points[0], points[3], points[4], points[5] };
    }

}
