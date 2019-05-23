package com.bitriddler.tictactoe.game;

import java.util.HashMap;

public class TicTacToeRepository {
    private HashMap<TicTacToeID, TicTacToe> games = new HashMap<>();
    private TicTacToe lastAddedGame;

    public TicTacToe getLastAddedGame() {
        return lastAddedGame;
    }

    public TicTacToe addGame(TicTacToeID id, TicTacToe game) {
        lastAddedGame = game;
        games.put(id, lastAddedGame);
        return lastAddedGame;
    }
}
