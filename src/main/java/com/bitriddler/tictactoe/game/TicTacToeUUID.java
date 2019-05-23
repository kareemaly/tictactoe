package com.bitriddler.tictactoe.game;

import java.util.UUID;

public class TicTacToeUUID implements TicTacToeID {
    public String get() {
        return UUID.randomUUID().toString();
    }
}
