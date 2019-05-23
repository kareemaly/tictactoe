package com.bitriddler.tictactoe.game.boardDisplay;

import com.bitriddler.tictactoe.game.GameBoard;

public interface BoardViewStrategy {
    public String get(GameBoard board);
}
