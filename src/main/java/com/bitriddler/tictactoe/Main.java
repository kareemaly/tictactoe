package com.bitriddler.tictactoe;

import com.bitriddler.tictactoe.game.*;

import java.io.*;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) {
        GameConfig gameConfig;
        Arguments programArgs;

        System.out.println("\n" +
                "  _   _      _             _             \n" +
                " | | (_)    | |           | |            \n" +
                " | |_ _  ___| |_ __ _  ___| |_ ___   ___ \n" +
                " | __| |/ __| __/ _` |/ __| __/ _ \\ / _ \\\n" +
                " | |_| | (__| || (_| | (__| || (_) |  __/\n" +
                "  \\__|_|\\___|\\__\\__,_|\\___|\\__\\___/ \\___|\n" +
                "                                         \n" +
                "                                         \n");
        try {
            programArgs = Arguments.build(args);
            // Build configurations from file
            gameConfig = GameConfig.buildFromFile(programArgs.getConfigurationFilename());
        } catch (InvalidGameConfigException | InvalidArgumentsException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        // Initialize factories and repository instances for game and player.
        TicTacToeRepository gameRepository = new TicTacToeRepository(new TicTacToeFactory(), gameConfig);
        PlayerFactory playerFactory = new PlayerFactory();

        try {
            ServerSocket serverSocket = new ServerSocket(programArgs.getPort());
            System.out.println("Server socket created and waiting for client connections");
            // Keep listening for connections
            while(true) {
                ClientSocketConnection clientSocketConnection = new ClientSocketConnection(serverSocket.accept());

                // Run a thread to handle socket connection
                Runnable handler = new SocketConnectionHandler(clientSocketConnection, playerFactory, gameRepository, gameConfig);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
