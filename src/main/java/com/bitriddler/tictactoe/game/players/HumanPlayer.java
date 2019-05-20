package com.bitriddler.tictactoe.game.players;

import com.bitriddler.tictactoe.game.GameIO;
import com.bitriddler.tictactoe.game.GameMove;
import com.bitriddler.tictactoe.game.boardDisplay.BoardViewStrategy;
import com.bitriddler.tictactoe.game.events.GameEvent;
import com.bitriddler.tictactoe.game.events.GameEventType;
import com.bitriddler.tictactoe.game.exceptions.InvalidMoveCommandException;
import com.bitriddler.tictactoe.game.exceptions.InvalidMoveException;
import com.bitriddler.tictactoe.game.moveParser.MoveParserStrategy;

public class HumanPlayer extends Player {
    private GameIO gameIO;
    private BoardViewStrategy boardViewStrategy;
    private MoveParserStrategy moveParserStrategy;

    HumanPlayer(char symbol, GameIO gameIO, BoardViewStrategy boardViewStrategy, MoveParserStrategy moveParserStrategy) {
        super(symbol);
        this.boardViewStrategy = boardViewStrategy;
        this.gameIO = gameIO;
        this.moveParserStrategy = moveParserStrategy;
    }

    private void askToMakeMove(GameEvent event) {
        try {
            GameMove move = moveParserStrategy.parse(gameIO.input());
            event.makeMove(this, move);
        } catch (InvalidMoveCommandException e) {
            gameIO.output("Command must be in this format: `x,y`");
            askToMakeMove(event);
        } catch (InvalidMoveException e) {
            gameIO.output(e.getMessage());
            askToMakeMove(event);
        }
    }

    private void handleGameStillOnEvents(GameEvent event) {
        gameIO.output(boardViewStrategy.get());
        Player playerToMove = event.getPlayerToMove();

        if (!this.equals(playerToMove)) {
            gameIO.output(String.format("Waiting for player (%c) to move", playerToMove.getSymbol()));
            return;
        }

        gameIO.output("Your Turn...");
        askToMakeMove(event);
    }

    private void handleGameEndedEvent(GameEvent event) {
        gameIO.output(boardViewStrategy.get());
        // No winner
        if (event.getWinner() == null) {
            gameIO.output("It's a draw...");
        }
        else if (event.getWinner().equals(this)) {
            gameIO.output("** winner winner chicken dinner **");
        } else {
            gameIO.output("-- better luck next time :'( --");
        }
        gameIO.disconnect();
    }

    @Override
    public void handleEvent(GameEventType eventType, GameEvent event) {
        switch (eventType) {
            case GAME_READY:
            case PLAYER_MOVED:
                this.handleGameStillOnEvents(event);
                break;
            case GAME_ENDED:
                this.handleGameEndedEvent(event);
                break;
        }
    }
}
