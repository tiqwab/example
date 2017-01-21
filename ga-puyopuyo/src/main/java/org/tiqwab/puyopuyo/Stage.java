package org.tiqwab.puyopuyo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiqwab.puyopuyo.scanner.BoardScanner;

import java.util.List;

/**
 * Created by nm on 1/20/17.
 */
public class Stage {

    private static final Logger logger = LoggerFactory.getLogger(Stage.class);

    private final int stageH;
    private Board board;

    public Stage(int[][] colors) {
        this(colors, colors.length);
    }

    public Stage(int[][] colors, int stageH) {
        assert stageH <= colors.length;
        this.board = new Board(colors);
        this.stageH = stageH;
    }

    public Board cycle(List<BoardScanner> scanners) {
        for (int i = 0; i < this.board.sizeY() - this.stageH; i++) {
           this.board = this.board.hideRow(i);
        }
        for (BoardScanner scanner : scanners) {
            scanner.scan(this.board);
        }
        this.board.delete();
        for (int i = 0; i < this.board.sizeY() - this.stageH; i++) {
            this.board = this.board.hideRow(i);
        }
        this.board = this.board.inverse().clean().inverse();
        return this.board;
    }

    public CycleResult cycles(List<BoardScanner> scanners) {
        int cycleCount = 0;
        // logger.debug("{}: \n{}", cycleCount, this.board.toString());
        while (true) {
            Board prev = new Board(this.board);
            cycle(scanners);
            if (prev.equals(this.board)) {
                break;
            }
            cycleCount += 1;
            // logger.debug("{}: \n{}", cycleCount, this.board.toString());
        }
        return new CycleResult(this.board, new ChainInfo(cycleCount));
    }

}
