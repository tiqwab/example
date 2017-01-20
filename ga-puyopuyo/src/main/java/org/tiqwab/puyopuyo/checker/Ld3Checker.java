package org.tiqwab.puyopuyo.checker;

import org.tiqwab.puyopuyo.scanner.Point;

/**
 * Created by nm on 1/18/17.
 * + + +
 * +
 */
public class Ld3Checker extends BlockCheckerBase {

    @Override
    public Point[] targetPoints(Point[] points) {
        return new Point[]{ points[0], points[1], points[2], points[3] };
    }

}
