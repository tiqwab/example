package org.tiqwab.puyopuyo.scanner;

import org.tiqwab.puyopuyo.checker.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nm on 1/18/17.
 * + + +
 * + + +
 */
public class ThreeByTwoScanner extends BoardScannerBase {

    public ThreeByTwoScanner(List<BlockChecker> checkers) {
        super(3, 2, checkers);
    }

    public static ThreeByTwoScanner newDefaultInstance() {
        List<BlockChecker> checkers = Arrays.asList(
                new Ld1Checker(), new Ld2Checker(), new Ld3Checker(), new Ld4Checker(),
                new T1Checker(), new T2Checker(),
                new Z1Checker(), new Z2Checker()
        );
        return new ThreeByTwoScanner(checkers);
    }

    @Override
    public Point[] selectPoints(int x, int y) {
        return new Point[]{
                new Point(x, y)     , new Point(x+1, y)      , new Point(x+2, y),
                new Point(x, y+1), new Point(x+1, y+1), new Point(x+2, y+1)
        };
    }

}
