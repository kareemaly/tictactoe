package com.bitriddler.tictactoe;

import com.bitriddler.tictactoe.game.*;
import com.bitriddler.tictactoe.game.exceptions.GameFullException;

import java.util.ArrayList;

class SocketConnectionHandler implements Runnable {
    private PlayerFactory playerFactory;
    private TicTacToeRepository gameRepository;
    private GameConfig gameConfig;
    private ClientSocketConnection clientSocketConnection;

    SocketConnectionHandler(ClientSocketConnection clientSocketConnection, PlayerFactory playerFactory, TicTacToeRepository gameRepository, GameConfig gameConfig) {
        this.clientSocketConnection = clientSocketConnection;
        this.playerFactory = playerFactory;
        this.gameRepository = gameRepository;
        this.gameConfig = gameConfig;
    }

    private TicTacToe initializeGame() throws GameFullException {
        TicTacToe game = gameRepository.getOrCreateGame();

        // New game then add the AI player
        if (game.getNumberOfConnectedPlayers() == 0) {
            Player aiPlayer = this.getAiPlayer(game);
            game.addSubscriberForAllEvents(aiPlayer);
            game.addPlayer(aiPlayer);
        }

        return game;
    }

    private Player initializePlayer(TicTacToe game) {
        // Get player symbol from configurations
        char playerSymbol = gameConfig.getPlayerSymbolAt(
                game.getNumberOfConnectedPlayers() - 1
        );

        // Create human player that stream to console
        return playerFactory.makeHumanPlayer(
                game.getBoard(),
                playerSymbol,
                clientSocketConnection
        );
    }

    private Player getAiPlayer(TicTacToe game) {
        Player[] hPlayers = game.getPlayers();
        return playerFactory.makeAiPlayer(
                gameConfig.getAiSymbol(),
                hPlayers
        );
    }

    @Override
    public void run() {
        TicTacToe game;
        Player player;

        try {
            game = this.initializeGame();
            player = this.initializePlayer(game);
            // Listen for all game events
            game.addSubscriberForAllEvents(player);
            // Add player to game
            game.addPlayer(player);
        } catch (GameFullException e) {
            // This exception should never happen since we checked if game is full before hand.
            clientSocketConnection.output("Game is full");
            clientSocketConnection.disconnect();
            return;
        }

        // Give some feedback to the user
        clientSocketConnection.output("Connected successfully");

        if (!game.isGameFull()) {
            clientSocketConnection.output("Waiting for players..");
        }

        System.out.println("New client connected");
    }
}
