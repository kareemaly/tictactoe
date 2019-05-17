package com.bitriddler.tictactoe.game;

public class InvalidGameConfigException extends Exception {
    InvalidGameConfigException(String message) {
        super(message);
    }
}
