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
    private final Stage stage;

    public Solver(Stage stage, List<BoardScanner> scanners) {
        this.stage = stage;
        this.scanners = scanners;
    }

    public Board cycle() {
        return this.stage.cycle(this.scanners);
    }

    public CycleResult cycles() {
        return stage.cycles(this.scanners);
    }
}
