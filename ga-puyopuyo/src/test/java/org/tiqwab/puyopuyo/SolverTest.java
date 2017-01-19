package org.tiqwab.puyopuyo;

import org.junit.Test;
import org.tiqwab.puyopuyo.checker.L1Checker;
import org.tiqwab.puyopuyo.checker.L2Checker;
import org.tiqwab.puyopuyo.scanner.BoardScanner;
import org.tiqwab.puyopuyo.scanner.TwoByThreeScanner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by nm on 1/19/17.
 */
public class SolverTest {

    @Test
    public void cycleTest() {
        // setup
        final int[][] colors = {
                { 1, 3, 2, 2 },
                { 1, 2, 3, 2 },
                { 1, 1, 3, 2 },
                { 1, 1, 3, 1 },
        };
        Board board = new Board(colors);

        List<BoardScanner> scanners = Arrays.asList(
                new TwoByThreeScanner(Arrays.asList(new L1Checker(), new L2Checker()))
        );

        Solver solver = new Solver(board, scanners);

        // exercise
        solver.cycle();

        // verify
        final int[][] expectedColors = {
                { 0, 3, 0, 0 },
                { 0, 2, 3, 0 },
                { 0, 0, 3, 0 },
                { 0, 0, 3, 1 },
        };
        Board expected = new Board(expectedColors);

        assertEquals(expected, board);
    }

}