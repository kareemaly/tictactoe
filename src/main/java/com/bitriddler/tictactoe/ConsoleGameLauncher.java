package com.bitriddler.tictactoe;

import com.bitriddler.tictactoe.game.*;
import com.bitriddler.tictactoe.game.ai.AiPlayerHandler;
import com.bitriddler.tictactoe.game.ai.BestMoveStrategy;
import com.bitriddler.tictactoe.game.ai.MinMaxBestMoveStrategy;
import com.bitriddler.tictactoe.game.boardDisplay.BoardBasicViewStrategy;
import com.bitriddler.tictactoe.game.exceptions.BoardSizeInvalidException;
import com.bitriddler.tictactoe.game.exceptions.GameFullException;
import com.bitriddler.tictactoe.game.moveParser.CommaMoveParserStrategy;
import com.bitriddler.tictactoe.game.players.Player;
import com.bitriddler.tictactoe.game.players.PlayerFactory;
import com.bitriddler.tictactoe.game.winner.StandardWinnerStrategy;
import com.bitriddler.tictactoe.game.winner.WinnerStrategy;

import java.io.IOException;
import java.net.ServerSocket;

public class ConsoleGameLauncher implements Runnable {
    private PlayerFactory playerFactory;
    private TicTacToeRepository gameRepository;
    private TicTacToeFactory gameFactory;
    private BestMoveStrategy bestMoveStrategy;
    private WinnerStrategy winnerStrategy;
    private GameConfig gameConfig;
    private int serverPort;

    public ConsoleGameLauncher(String configFilename, int serverPort) {
        this.initializeGameConfig(configFilename);
        this.initializeInstances();
        this.serverPort = serverPort;
    }

    private void initializeInstances() {
        // Initialize factories and repository instances for game and player.
        gameFactory = new TicTacToeFactory();
        gameRepository = new TicTacToeRepository();
        playerFactory = new PlayerFactory();
        winnerStrategy = new StandardWinnerStrategy();
        bestMoveStrategy = new MinMaxBestMoveStrategy(winnerStrategy);
    }

    private void initializeGameConfig(String configFilename) {
        try {
            // Build configurations from file
            gameConfig = GameConfig.buildFromFile(configFilename);
        } catch (InvalidGameConfigException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }
    }

    private Player initializePlayer(TicTacToe game) throws GameFullException {
        // Get player symbol from configurations
        char playerSymbol = gameConfig.getPlayerSymbolAt(
                game.getNumberOfConnectedPlayers() - 1
        );

        Player player = playerFactory.makeHumanPlayer(playerSymbol);
        game.addPlayer(player);
        return player;
    }

    private TicTacToe initializeGame() throws GameFullException {
        TicTacToe currentGame = gameRepository.getLastAddedGame();

        try {
            // Create a new game
            if (currentGame == null || currentGame.isGameFull()) {
                currentGame = gameFactory.build(gameConfig.getBoardSize());
                gameRepository.addGame(new TicTacToeUUID(), currentGame);
            }
        } catch (BoardSizeInvalidException e) {
            e.printStackTrace();
        }

        // New game then add the AI player
        if (currentGame.getNumberOfConnectedPlayers() == 0) {
            Player aiPlayer = playerFactory.makeAiPlayer(gameConfig.getAiSymbol());
            AiPlayerHandler aiPlayerHandler = new AiPlayerHandler(aiPlayer, bestMoveStrategy);
            currentGame.addSubscriberForAllEvents(aiPlayerHandler);
            currentGame.addPlayer(aiPlayer);
        }

        return currentGame;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Server socket created and waiting for client connections");
            // Keep listening for connections
            while(true) {
                ClientSocketConnection clientSocketConnection = new ClientSocketConnection(serverSocket.accept());

                TicTacToe game = this.initializeGame();
                // Run a thread to handle socket connection
                SocketConnectionHandler handler = new SocketConnectionHandler(
                        clientSocketConnection,
                        game,
                        this.initializePlayer(game),
                        new CommaMoveParserStrategy(),
                        new BoardBasicViewStrategy()
                );
                game.addSubscriberForAllEvents(handler);

                // Give some feedback to the user
                clientSocketConnection.output("Connected successfully");

                if (!game.isGameFull()) {
                    clientSocketConnection.output("Waiting for players..");
                }

                System.out.println("New player connected!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (GameFullException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
