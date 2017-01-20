package org.tiqwab.puyopuyo.scanner;

import org.tiqwab.puyopuyo.checker.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nm on 1/18/17.
 * + +
 * + +
 */
public class TwoByTwoScanner extends BoardScannerBase {

    public TwoByTwoScanner(List<BlockChecker> checkers) {
        super(2, 2, checkers);
    }

    public static TwoByTwoScanner newDefaultInstance() {
        List<BlockChecker> checkers = Arrays.asList(
                new O1Checker()
        );
        return new TwoByTwoScanner(checkers);
    }

    @Override
    public Point[] selectPoints(int x, int y) {
        return new Point[]{
                new Point(x, y)     , new Point(x+1, y),
                new Point(x, y+1), new Point(x+1, y+1)
        };
    }

}
