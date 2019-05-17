package com.bitriddler.tictactoe.game.winner;

import com.bitriddler.tictactoe.game.GameBoard;
import com.bitriddler.tictactoe.game.Player;

import java.util.ArrayList;

public interface WinnerStrategy {
    public Player getWinner(GameBoard board, ArrayList<Player> players);
}
