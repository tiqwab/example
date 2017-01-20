package org.tiqwab.puyopuyo.scanner;

import org.tiqwab.puyopuyo.checker.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nm on 1/18/17.
 * + +
 * + +
 * + +
 */
public class TwoByThreeScanner extends BoardScannerBase {

    public TwoByThreeScanner(List<BlockChecker> checkers) {
        super(2, 3, checkers);
    }

    public static TwoByThreeScanner newDefaultInstance() {
        List<BlockChecker> checkers = Arrays.asList(
                new L1Checker(), new L2Checker(), new L3Checker(), new L4Checker(),
                new Td1Checker(), new Td2Checker(),
                new Zd1Checker(), new Zd2Checker()
        );
        return new TwoByThreeScanner(checkers);
    }

    @Override
    public Point[] selectPoints(int x, int y) {
        return new Point[]{
                new Point(x, y)     , new Point(x+1, y),
                new Point(x, y+1), new Point(x+1, y+1),
                new Point(x, y+2), new Point(x+1, y+2)
        };
    }

}
