package org.tiqwab.puyopuyo;

import org.junit.Test;
import org.tiqwab.puyopuyo.checker.L1Checker;
import org.tiqwab.puyopuyo.checker.L2Checker;
import org.tiqwab.puyopuyo.scanner.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by nm on 1/19/17.
 */
public class SolverTest {

    @Test
    public void cycleTestWithTwoByThree() {
        // setup
        final int[][] colors = {
                { 1, 1, 9, 9, 2, 2, },
                { 1, 9, 1, 2, 9, 2, },
                { 1, 9, 1, 2, 9, 2, },
                { 9, 1, 1, 2, 2, 1, },
                { 9, 9, 2, 9, 1, 1, },
                { 1, 2, 2, 2, 1, 9, },
                { 1, 1, 2, 2, 2, 9, },
                { 1, 9, 9, 8, 2, 9, },
        };
        Board board = new Board(colors);
        List<BoardScanner> scanners = Arrays.asList(TwoByThreeScanner.newDefaultInstance());
        Solver solver = new Solver(board, scanners);

        // exercise
        Board actual = solver.cycle();

        // verify
        final int[][] expectedColors = {
                { 0, 0, 0, 0, 0, 0, },
                { 0, 0, 0, 0, 0, 0, },
                { 0, 0, 0, 0, 0, 0, },
                { 0, 0, 0, 0, 0, 0, },
                { 0, 9, 0, 0, 0, 0, },
                { 0, 9, 0, 9, 0, 9, },
                { 9, 9, 9, 9, 9, 9, },
                { 9, 9, 9, 8, 9, 9, },
        };
        Board expected = new Board(expectedColors);
        assertEquals(expected, actual);
    }

    @Test
    public void cycleTestWithThreeByTwo() {
        // setup
        final int[][] colors = {
                { 1, 1, 1, 9, 8, 9, 1, 1, 1 },
                { 1, 9, 9, 1, 9, 1, 9, 9, 1 },
                { 9, 1, 1, 1, 9, 1, 1, 1, 8 },
                { 9, 9, 8, 2, 2, 9, 9, 8, 8 },
                { 1, 1, 2, 2, 9, 1, 2, 2, 2 },
                { 9, 1, 1, 9, 1, 1, 1, 2, 8 },
        };
        Board board = new Board(colors);
        List<BoardScanner> scanners = Arrays.asList(ThreeByTwoScanner.newDefaultInstance());
        Solver solver = new Solver(board, scanners);

        // exercise
        Board actual = solver.cycle();

        // verify
        final int[][] expectedColors = {
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 8, 0, 0, 0, 0 },
                { 9, 0, 0, 0, 9, 0, 0, 0, 8 },
                { 9, 9, 9, 9, 9, 9, 9, 9, 8 },
                { 9, 9, 8, 9, 9, 9, 9, 8, 8 },
        };
        Board expected = new Board(expectedColors);
        assertEquals(expected, actual);
    }

    @Test
    public void cycleTestWithTwoByTwo() {
        // setup
        final int[][] colors = {
                { 9, 9, 1, 1 },
                { 2, 2, 1, 1 },
                { 2, 2, 9, 9 },
        };
        Board board = new Board(colors);
        List<BoardScanner> scanners = Arrays.asList(TwoByTwoScanner.newDefaultInstance());
        Solver solver = new Solver(board, scanners);

        // exercise
        Board actual = solver.cycle();

        // verify
        final int[][] expectedColors = {
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 9, 9, 9, 9 },
        };
        Board expected = new Board(expectedColors);
        assertEquals(expected, actual);
    }

    @Test
    public void cycleTestWithFourByOne() {
        // setup
        final int[][] colors = {
                { 9, 9, 8, 8 },
                { 8, 8, 9, 9 },
                { 1, 1, 1, 1 },
        };
        Board board = new Board(colors);
        List<BoardScanner> scanners = Arrays.asList(FourByOneScanner.newDefaultInstance());
        Solver solver = new Solver(board, scanners);

        // exercise
        Board actual = solver.cycle();

        // verify
        final int[][] expectedColors = {
                { 0, 0, 0, 0 },
                { 9, 9, 8, 8 },
                { 8, 8, 9, 9 },
        };
        Board expected = new Board(expectedColors);
        assertEquals(expected, actual);
    }

    @Test
    public void cycleTestWithOneByFour() {
        // setup
        final int[][] colors = {
                { 1, 9, 8, 9 },
                { 1, 9, 8, 9 },
                { 1, 8, 9, 8 },
                { 1, 8, 9, 8 },
        };
        Board board = new Board(colors);
        List<BoardScanner> scanners = Arrays.asList(OneByFourScanner.newDefaultInstance());
        Solver solver = new Solver(board, scanners);

        // exercise
        Board actual = solver.cycle();

        // verify
        final int[][] expectedColors = {
                { 0, 9, 8, 9 },
                { 0, 9, 8, 9 },
                { 0, 8, 9, 8 },
                { 0, 8, 9, 8 },
        };
        Board expected = new Board(expectedColors);
        assertEquals(expected, actual);
    }
}