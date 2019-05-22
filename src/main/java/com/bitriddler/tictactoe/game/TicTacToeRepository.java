package com.bitriddler.tictactoe.game;

import java.util.ArrayList;

public class TicTacToeRepository {
    private ArrayList<TicTacToe> games = new ArrayList<>();
    private TicTacToe lastAddedGame;

    public TicTacToe getLastAddedGame() {
        return lastAddedGame;
    }

    public TicTacToe addGame(TicTacToe game) {
        lastAddedGame = game;
        games.add(lastAddedGame);
        return lastAddedGame;
    }
}
