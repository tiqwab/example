package org.tiqwab.puyopuyo.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiqwab.puyopuyo.Board;
import org.tiqwab.puyopuyo.Puyo;
import org.tiqwab.puyopuyo.checker.BlockChecker;

import java.util.List;

/**
 * Created by nm on 1/19/17.
 */
public abstract class BoardScannerBase implements BoardScanner {

    private static final Logger logger = LoggerFactory.getLogger(BoardScannerBase.class);

    private final int space_x;
    private final int space_y;
    private final List<BlockChecker> checkers;

    public BoardScannerBase(int space_x, int space_y, List<BlockChecker> checkers) {
        this.space_x = space_x;
        this.space_y = space_y;
        this.checkers = checkers;
    }

    @Override
    public void scan(Board board) {
        final int size_x = board.sizeX();
        final int size_y = board.sizeY();
        for (int y = 0; y < size_y - space_y + 1; y++) {
            for (int x = 0; x < size_x - space_x + 1; x++) {
                Point[] pointsInArea = selectPoints(x, y);
                for (BlockChecker checker : this.checkers) {
                    // logger.debug("Checker: {}, Point: {}", checker.getClass(), pointsInArea);
                    checker.check(pointsInArea, board);
                }
            }
        }
    }

}
