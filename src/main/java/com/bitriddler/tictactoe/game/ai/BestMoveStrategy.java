package com.bitriddler.tictactoe.game.ai;

import com.bitriddler.tictactoe.game.GameBoard;
import com.bitriddler.tictactoe.game.GameMove;

public interface BestMoveStrategy {
    GameMove findBestMove(GameBoard position, int depth);
}
