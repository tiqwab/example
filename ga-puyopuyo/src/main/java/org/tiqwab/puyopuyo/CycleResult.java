package org.tiqwab.puyopuyo;

/**
 * Created by nm on 1/21/17.
 */
public class CycleResult {

    public final Board board;
    public final ChainInfo chainInfo;

    public CycleResult(Board board, ChainInfo chainInfo) {
        this.board = board;
        this.chainInfo = chainInfo;
    }

}
