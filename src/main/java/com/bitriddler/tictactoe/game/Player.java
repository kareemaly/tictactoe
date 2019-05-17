package com.bitriddler.tictactoe.game;

import com.bitriddler.tictactoe.game.events.GameEventSubscriber;

public abstract class Player implements GameEventSubscriber {
    private char symbol;

    public Player(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
