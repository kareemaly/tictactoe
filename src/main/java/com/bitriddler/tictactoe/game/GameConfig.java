package com.bitriddler.tictactoe.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class GameConfig {
    private int boardSize;
    private char[] playerSymbols;
    private char aiSymbol;

    GameConfig(int boardSize, char[] playerSymbols, char aiSymbol) {
        this.boardSize = boardSize;
        this.playerSymbols = playerSymbols;
        this.aiSymbol = aiSymbol;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public char getAiSymbol() { return aiSymbol; }

    public char getPlayerSymbolAt(int index) {
        return playerSymbols[index];
    }

    public static GameConfig buildFromFile(String pathname) throws InvalidGameConfigException {
        File file = new File(pathname);

        Scanner sc;

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new InvalidGameConfigException("Give file was not found");
        }

        if (!sc.hasNextLine()) {
            throw new InvalidGameConfigException("Config file can't be empty");
        }

        char[] config = sc.nextLine().toCharArray();

        int boardSize;
        char[] humanSymbols;
        char aiSymbol;

        if (config.length == 4) {
            boardSize = Character.getNumericValue(config[0]);
            humanSymbols = Arrays.copyOfRange(config, 1, 3);
            aiSymbol = config[3];
        } else if (config.length == 5) {
            boardSize = Integer.parseInt("" + config[0] + config[1]);
            humanSymbols = Arrays.copyOfRange(config, 2, 4);
            aiSymbol = config[4];
        } else {
            throw new InvalidGameConfigException("Config file must exactly contain 4 or 5 chars... {board-size}{player1}{player2}{ai-player}");
        }

        return new GameConfig(boardSize, humanSymbols, aiSymbol);
    }
}
