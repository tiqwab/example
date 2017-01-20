package org.tiqwab.puyopuyo.scanner;

import org.tiqwab.puyopuyo.checker.BlockChecker;
import org.tiqwab.puyopuyo.checker.Id1Checker;
import org.tiqwab.puyopuyo.checker.O1Checker;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nm on 1/18/17.
 * + + + +
 */
public class FourByOneScanner extends BoardScannerBase {

    public FourByOneScanner(List<BlockChecker> checkers) {
        super(4, 1, checkers);
    }

    public static FourByOneScanner newDefaultInstance() {
        List<BlockChecker> checkers = Arrays.asList(
                new Id1Checker()
        );
        return new FourByOneScanner(checkers);
    }

    @Override
    public Point[] selectPoints(int x, int y) {
        return new Point[]{
                new Point(x, y), new Point(x+1, y), new Point(x+2, y), new Point(x+3, y)
        };
    }

}
