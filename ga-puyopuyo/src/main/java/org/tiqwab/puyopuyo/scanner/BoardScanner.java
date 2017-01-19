package org.tiqwab.puyopuyo.scanner;

import org.tiqwab.puyopuyo.Board;

/**
 * Created by nm on 1/18/17.
 */
public interface BoardScanner {
    public Point[] selectPoints(int x, int y);
    public void scan(Board board);
}
