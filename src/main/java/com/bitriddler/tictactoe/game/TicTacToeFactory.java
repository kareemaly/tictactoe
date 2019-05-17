package com.bitriddler.tictactoe.game;

import com.bitriddler.tictactoe.game.exceptions.BoardSizeInvalidException;
import com.bitriddler.tictactoe.game.winner.StandardWinnerStrategy;

public class TicTacToeFactory {
    public TicTacToe build(int boardSize) throws BoardSizeInvalidException {
        StandardWinnerStrategy getWinnerStrategy = new StandardWinnerStrategy();
        return new TicTacToe(boardSize, getWinnerStrategy);
    }
}
