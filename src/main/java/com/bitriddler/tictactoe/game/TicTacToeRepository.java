package com.bitriddler.tictactoe.game;

import com.bitriddler.tictactoe.game.exceptions.BoardSizeInvalidException;

import java.util.ArrayList;

public class TicTacToeRepository {
    private TicTacToeFactory factory;
    private ArrayList<TicTacToe> games = new ArrayList<>();
    private GameConfig gameConfig;

    public TicTacToeRepository(TicTacToeFactory factory, GameConfig gameConfig) {
        this.factory = factory;
        this.gameConfig = gameConfig;
    }

    private TicTacToe getLastCreatedGame() {
        return games.size() > 0 ? games.get(games.size() - 1) : null;
    }

    public TicTacToe getOrCreateGame() {
        TicTacToe lastCreatedGame = getLastCreatedGame();
        // Create a new game
        if (lastCreatedGame == null || lastCreatedGame.isGameFull()) {
            try {
                lastCreatedGame = factory.build(gameConfig.getBoardSize());
            } catch (BoardSizeInvalidException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        games.add(lastCreatedGame);

        return lastCreatedGame;
    }
}
