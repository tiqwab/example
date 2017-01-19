package org.tiqwab.puyopuyo;

import org.tiqwab.puyopuyo.checker.L1Checker;
import org.tiqwab.puyopuyo.scanner.BoardScanner;
import org.tiqwab.puyopuyo.scanner.TwoByThreeScanner;

import java.util.List;

/**
 * Created by nm on 1/18/17.
 */
public class Solver {

    private final List<BoardScanner> scanners;
    private Board board;

    public Solver(Board board, List<BoardScanner> scanners) {
        this.board = board;
        this.scanners = scanners;
    }

    public static void main(String[] args) throws Exception {
        int[][] colors = {
                {1, 2, 2, 2},
                {1, 3, 3, 3},
                {1, 1, 2, 2},
                {1, 1, 3, 3}
        };

        Board board = new Board(colors);

        System.out.println(board);
    }

    public void cycle() {
        for (BoardScanner scanner : this.scanners) {
            scanner.scan(this.board);
        }
        this.board = this.board.delete().inverse().clean().inverse();
    }
}
