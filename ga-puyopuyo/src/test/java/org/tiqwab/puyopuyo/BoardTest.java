package org.tiqwab.puyopuyo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by nm on 1/19/17.
 */
public class BoardTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void inverseTest() {
        // setup
        Board board = new Board(new int[][] {
                { 1, 2, 3, 4 },
                { 2, 3, 3, 4 },
                { 1, 2, 2, 4 },
                { 4, 3, 2, 4 },
                { 3, 3, 2, 4 },
        });

        // exercise
        Board actual = board.inverse();

        // verify
        Board expected = new Board(new int[][] {
                { 1, 2, 1, 4, 3 },
                { 2, 3, 2, 3, 3 },
                { 3, 3, 2, 2, 2 },
                { 4, 4, 4, 4, 4 },
        });
        assertEquals(expected, actual);
    }

    @Test
    public void cleanTest() {
        // setup
        Board board = new Board(new int[][] {
                { 1, 2, 0, 0 },
                { 0, 1, 3, 4 },
                { 2, 0, 0, 4 },
                { 1, 2, 2, 4 },
                { 4, 0, 0, 0 },
        });

        // exercise
        Board actual = board.clean();

        // verify
        Board expected = new Board(new int[][] {
                { 0, 0, 1, 2 },
                { 0, 1, 3, 4 },
                { 0, 0, 2, 4 },
                { 1, 2, 2, 4 },
                { 0, 0, 0, 4 },
        });
        assertEquals(expected, actual);

    }

    @Test
    public void deleteTest() {
        // setup
        Board board = new Board(new int[][] {
                { 4, 0, 0, 0 },
                { 1, 2, 3, 4 },
                { 1, 3, 4, 2 },
                { 1, 2, 4, 4 },
                { 1, 2, 3, 4 },
        });

        board.willDelete(0, 1); board.willDelete(0, 2); board.willDelete(0, 3); board.willDelete(0, 4);
        board.willDelete(2, 2); board.willDelete(2, 3); board.willDelete(3, 3); board.willDelete(3, 4);

        // exercise
        board.delete();

        // verify
        Board expected = new Board(new int[][] {
                { 4, 0, 0, 0 },
                { 0, 2, 3, 4 },
                { 0, 3, 0, 2 },
                { 0, 2, 0, 0 },
                { 0, 2, 3, 0 },
        });
        assertEquals(expected, board);
    }

}