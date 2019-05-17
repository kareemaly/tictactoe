package com.bitriddler.tictactoe.game;

import com.bitriddler.tictactoe.game.boardDisplay.BoardViewStrategy;
import com.bitriddler.tictactoe.game.events.GameEventSubscriber;
import com.bitriddler.tictactoe.game.moveParser.MoveParserStrategy;

public abstract class Player implements GameEventSubscriber {
    private char symbol;
    protected GameIO gameIO;
    protected BoardViewStrategy boardViewStrategy;
    protected MoveParserStrategy moveParserStrategy;

    public Player(char symbol) {
        this.symbol = symbol;
    }

    public Player(char symbol, GameIO gameIO, BoardViewStrategy boardViewStrategy, MoveParserStrategy moveParserStrategy) {
        this.symbol = symbol;
        this.boardViewStrategy = boardViewStrategy;
        this.gameIO = gameIO;
        this.moveParserStrategy = moveParserStrategy;
    }

    public char getSymbol() {
        return symbol;
    }
}
