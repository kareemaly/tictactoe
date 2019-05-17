package com.bitriddler.tictactoe.game.moveParser;

import com.bitriddler.tictactoe.game.GameMove;
import com.bitriddler.tictactoe.game.exceptions.InvalidMoveCommandException;

public class CommaMoveParserStrategy implements MoveParserStrategy {
    @Override
    public GameMove parse(String command) throws InvalidMoveCommandException {
        String[] positions = command.split(",");

        if (positions.length != 2) {
            throw new InvalidMoveCommandException();
        }

        int x, y;

        try {
            x = Integer.parseInt(positions[0]);
            y = Integer.parseInt(positions[1]);
        } catch (NumberFormatException e) {
            throw new InvalidMoveCommandException();
        }

        return new GameMove(x, y);
    }
}
