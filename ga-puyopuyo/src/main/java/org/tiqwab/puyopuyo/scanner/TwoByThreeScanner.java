package org.tiqwab.puyopuyo.scanner;

import org.tiqwab.puyopuyo.Board;
import org.tiqwab.puyopuyo.Puyo;
import org.tiqwab.puyopuyo.checker.BlockChecker;

import java.util.List;

/**
 * Created by nm on 1/18/17.
 */
public class TwoByThreeScanner extends BoardScannerBase {

    public TwoByThreeScanner(List<BlockChecker> checkers) {
        super(2, 3, checkers);
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
