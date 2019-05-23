package com.bitriddler.tictactoe;

import com.bitriddler.tictactoe.game.*;
import com.bitriddler.tictactoe.game.boardDisplay.BoardViewStrategy;
import com.bitriddler.tictactoe.game.events.GameEvent;
import com.bitriddler.tictactoe.game.events.GameEventSubscriber;
import com.bitriddler.tictactoe.game.events.GameEventType;
import com.bitriddler.tictactoe.game.exceptions.InvalidMoveCommandException;
import com.bitriddler.tictactoe.game.exceptions.InvalidMoveException;
import com.bitriddler.tictactoe.game.moveParser.MoveParserStrategy;
import com.bitriddler.tictactoe.game.players.Player;

class SocketConnectionHandler implements GameEventSubscriber {
    private ClientSocketConnection clientSocketConnection;
    private TicTacToe game;
    private Player player;
    private MoveParserStrategy moveParserStrategy;
    private BoardViewStrategy boardViewStrategy;

    public SocketConnectionHandler(
            ClientSocketConnection clientSocketConnection,
            TicTacToe game,
            Player player,
            MoveParserStrategy moveParserStrategy,
            BoardViewStrategy boardViewStrategy
    ) {
        this.clientSocketConnection = clientSocketConnection;
        this.game = game;
        this.player = player;
        this.moveParserStrategy = moveParserStrategy;
        this.boardViewStrategy = boardViewStrategy;
    }

    Player getPlayer() {
        return player;
    }

    private void askToMakeMove(GameEvent event) {
        try {
            GameMove move = moveParserStrategy.parse(clientSocketConnection.input());
            event.makeMove(player, move);
        } catch (InvalidMoveCommandException e) {
            clientSocketConnection.output("Command must be in this format: `x,y`");
            askToMakeMove(event);
        } catch (InvalidMoveException e) {
            clientSocketConnection.output(e.getMessage());
            askToMakeMove(event);
        }
    }

    private void handleGameStillOnEvents(GameEvent event) {
        clientSocketConnection.output(boardViewStrategy.get(game.getBoard()));
        Player playerToMove = event.getPlayerToMove();

        if (!player.equals(playerToMove)) {
            clientSocketConnection.output(String.format("Waiting for player (%c) to move", playerToMove.getSymbol()));
            return;
        }

        clientSocketConnection.output("Your Turn...");
        askToMakeMove(event);
    }

    private void handleGameEndedEvent(GameEvent event) {
        clientSocketConnection.output(boardViewStrategy.get(game.getBoard()));
        // No winner
        if (event.getWinner() == null) {
            clientSocketConnection.output("It's a draw...");
        }
        else if (event.getWinner().equals(this)) {
            clientSocketConnection.output("** winner winner chicken dinner **");
        } else {
            clientSocketConnection.output("-- better luck next time :'( --");
        }
        clientSocketConnection.disconnect();
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
