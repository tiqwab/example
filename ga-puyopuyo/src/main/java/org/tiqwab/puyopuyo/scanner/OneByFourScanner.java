package org.tiqwab.puyopuyo.scanner;

import org.tiqwab.puyopuyo.checker.BlockChecker;
import org.tiqwab.puyopuyo.checker.I1Checker;
import org.tiqwab.puyopuyo.checker.O1Checker;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nm on 1/18/17.
 * +
 * +
 * +
 * +
 */
public class OneByFourScanner extends BoardScannerBase {

    public OneByFourScanner(List<BlockChecker> checkers) {
        super(1, 4, checkers);
    }

    public static OneByFourScanner newDefaultInstance() {
        List<BlockChecker> checkers = Arrays.asList(
                new I1Checker()
        );
        return new OneByFourScanner(checkers);
    }

    @Override
    public Point[] selectPoints(int x, int y) {
        return new Point[]{
                new Point(x, y),
                new Point(x, y+1),
                new Point(x, y+2),
                new Point(x, y+3)
        };
    }

}
