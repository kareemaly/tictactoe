package com.bitriddler.tictactoe.game.moveParser;

import com.bitriddler.tictactoe.game.GameMove;
import com.bitriddler.tictactoe.game.exceptions.InvalidMoveCommandException;
import com.bitriddler.tictactoe.game.exceptions.InvalidMoveException;

public interface MoveParserStrategy {
    public GameMove parse(String command) throws InvalidMoveCommandException, InvalidMoveException;
}
