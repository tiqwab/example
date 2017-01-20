package org.tiqwab.puyopuyo;

import org.junit.Test;
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
        Stage stage = new Stage(colors);
        List<BoardScanner> scanners = Arrays.asList(TwoByThreeScanner.newDefaultInstance());
        Solver solver = new Solver(stage, scanners);

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
        Stage stage = new Stage(colors);
        List<BoardScanner> scanners = Arrays.asList(ThreeByTwoScanner.newDefaultInstance());
        Solver solver = new Solver(stage, scanners);

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
        Stage stage = new Stage(colors);
        List<BoardScanner> scanners = Arrays.asList(TwoByTwoScanner.newDefaultInstance());
        Solver solver = new Solver(stage, scanners);

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
        Stage stage = new Stage(colors);
        List<BoardScanner> scanners = Arrays.asList(FourByOneScanner.newDefaultInstance());
        Solver solver = new Solver(stage, scanners);

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
        Stage stage = new Stage(colors);
        List<BoardScanner> scanners = Arrays.asList(OneByFourScanner.newDefaultInstance());
        Solver solver = new Solver(stage, scanners);

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

    @Test
    public void cyclesTest() {
        // setup
        final int[][] colors = {
                { 1, 2, 0, 0, 2, 2 },
                { 1, 1, 1, 3, 2, 1 },
                { 3, 1, 2, 2, 3, 2 },
                { 1, 2, 3, 3, 2, 1 },
                { 2, 3, 2, 1, 3, 1 },
                { 3, 2, 1, 3, 2, 1 },
                { 2, 3, 2, 1, 3, 2 },
                { 2, 3, 2, 1, 3, 2 },
                { 2, 1, 1, 3, 2, 3 },
                { 1, 2, 3, 1, 3, 3 },
                { 3, 1, 2, 3, 1, 2 },
                { 3, 1, 2, 3, 1, 2 },
                { 3, 1, 2, 3, 1, 2 },
        };
        Stage stage = new Stage(colors, colors.length - 1);
        List<BoardScanner> scanners = Arrays.asList(
                OneByFourScanner.newDefaultInstance(), FourByOneScanner.newDefaultInstance(),
                TwoByThreeScanner.newDefaultInstance(), ThreeByTwoScanner.newDefaultInstance(),
                TwoByTwoScanner.newDefaultInstance()
        );
        Solver solver = new Solver(stage, scanners);

        // exercise
        CycleResult cr = solver.cycles();

        // verify
        assertTrue(cr.board.isAllEmpty());
        assertEquals(19, cr.chainInfo.chainCount);
    }
}