package com.bitriddler.tictactoe.game.ai;

import com.bitriddler.tictactoe.game.GameBoard;
import com.bitriddler.tictactoe.game.GameMove;
import com.bitriddler.tictactoe.game.players.Player;

public interface BestMoveStrategy {
    GameMove findBestMove(Player[] opponentPlayers, Player player, GameBoard position, int depth);
}
