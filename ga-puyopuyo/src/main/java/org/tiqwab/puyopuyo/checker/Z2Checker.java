package org.tiqwab.puyopuyo.checker;

import org.tiqwab.puyopuyo.scanner.Point;

/**
 * Created by nm on 1/20/17.
 *   + +
 * + +
 */
public class Z2Checker extends BlockCheckerBase {
    @Override
    public Point[] targetPoints(Point[] points) {
        return new Point[]{ points[1], points[2], points[3], points[4] };
    }
}
