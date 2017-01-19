package org.tiqwab.puyopuyo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiqwab.puyopuyo.scanner.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Puyopuyo board has 6x12 on the display, but 6x13 is necessary to perform 19 chains.
 */
public class Board {

    private static final Logger logger = LoggerFactory.getLogger(Board.class);

    private Puyo[][] board;

    public Board(int[][] colors) {
        final int size_x = colors[0].length;
        final int size_y = colors.length;
        this.board = new Puyo[size_y][];
        for (int i = 0; i < size_y; i++) {
            this.board[i] = new Puyo[size_x];
            for (int j = 0; j < size_x; j++) {
                this.board[i][j] = new Puyo(colors[i][j]);
            }
        }
    }

    public Board(Puyo[][] puyos) {
        this.board = puyos;
    }

    public Puyo get(int x, int y) {
        return board[y][x];
    }

    public int sizeX() {
        return board.length;
    }

    public int sizeY() {
        return board[0].length;
    }

    @Override
    public String toString() {
        List<String> lines = new ArrayList<>();
        for (Puyo[] ps : this.board) {
            List<String> ss = Arrays.stream(ps).map(p -> p.toString()).collect(Collectors.toList());
            lines.add(String.join(" ", ss));
        }
        return String.join(System.lineSeparator(), lines);
    }

    public String toStringVerbose() {
        List<String> lines = new ArrayList<>();
        for (Puyo[] ps : this.board) {
            List<String> ss = Arrays.stream(ps).map(p -> p.toStringVerbose()).collect(Collectors.toList());
            lines.add(String.join(" ", ss));
        }
        return String.join(System.lineSeparator(), lines);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board1 = (Board) o;

        return Arrays.deepEquals(board, board1.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Not generate all puyos, but reuse.
     * @return
     */
    public Board inverse() {
        final Puyo[][] puyos = new Puyo[this.board[0].length][];
        for (int i = 0; i < this.board[0].length; i++) {
            puyos[i] = new Puyo[this.board.length];
            for (int j = 0; j < this.board.length; j++) {
                puyos[i][j] = this.board[j][i];
            }
        }
        return new Board(puyos);
    }

    /**
     * Not generate all puyos, but reuse.
     * @return
     */
    public Board clean() {
        Puyo[][] cleaned = new Puyo[this.board.length][];
        for (int i = 0; i < this.board.length; i++) {
            cleaned[i] = new Puyo[this.board[i].length];
            Arrays.fill(cleaned[i], new Puyo(0));
            List<Puyo> values = new ArrayList<>();
            for (Puyo puyo : this.board[i]) {
                if (!puyo.deleted()) {
                    values.add(puyo);
                }
            }
            for (int j = 0; j < values.size(); j++) {
                cleaned[i][cleaned[i].length - values.size() + j] = values.get(j);
            }
        }
        return new Board(cleaned);
    }

    public void willDelete(int x, int y) {
        this.board[y][x] = this.board[y][x].willDelete();
    }

    public Board delete() {
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (this.board[i][j].willBeDeleted()) {
                    this.board[i][j] = Puyo.emptyPuyo();
                }
            }
        }
        return this;
    }

    public void checkPuyos(Point[] points) {
        List<Puyo> puyos = Arrays.stream(points).map(p -> this.get(p.x, p.y)).collect(Collectors.toList());
        logger.debug("try check: " + Arrays.toString(points));
        if (Puyo.eauqlAll(puyos)) {
            logger.debug("Equal!");
            for (int i = 0; i < points.length; i++) {
                this.board[points[i].y][points[i].x] = this.board[points[i].y][points[i].x].willDelete();
            }
        }
    }
}
