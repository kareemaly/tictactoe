package com.bitriddler.tictactoe.game.winner;

import com.bitriddler.tictactoe.game.GameBoard;
import com.bitriddler.tictactoe.game.players.Player;

public interface WinnerStrategy {
    public Player getWinner(GameBoard board, Player[] players);
}
