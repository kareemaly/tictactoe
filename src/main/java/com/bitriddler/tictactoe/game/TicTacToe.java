package com.bitriddler.tictactoe.game;

import com.bitriddler.tictactoe.game.events.GameEvent;
import com.bitriddler.tictactoe.game.events.GameEventSubscriber;
import com.bitriddler.tictactoe.game.events.GameEventType;
import com.bitriddler.tictactoe.game.exceptions.BoardSizeInvalidException;
import com.bitriddler.tictactoe.game.exceptions.GameFullException;
import com.bitriddler.tictactoe.game.exceptions.InvalidMoveException;
import com.bitriddler.tictactoe.game.winner.WinnerStrategy;

import java.util.ArrayList;

public class TicTacToe {
    private ArrayList<Player> players = new ArrayList<>();
    private GameBoard board;
    private int playerToMoveIndex = 0;
    private WinnerStrategy winnerStrategy;
    private final int maxNoOfPlayers = 3;
    private GameEventPublisher gameEventPublisher;

    public TicTacToe(int boardSize, WinnerStrategy winnerStrategy) throws BoardSizeInvalidException {
        if (boardSize > 10 || boardSize < 3) {
            throw new BoardSizeInvalidException("TicTacToe size must be between 3 and 10");
        }
        this.winnerStrategy = winnerStrategy;
        this.board = new GameBoard(boardSize);
        this.gameEventPublisher = new GameEventPublisher();
    }

    public GameBoard getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getNumberOfConnectedPlayers() {
        return players.size();
    }

    public void addSubscriberFor(GameEventType eventType, GameEventSubscriber subscriber) {
        gameEventPublisher.addSubscriberFor(eventType, subscriber);
    }

    public void addSubscriberForAllEvents(GameEventSubscriber subscriber) {
        gameEventPublisher.addSubscriberForAllEvents(subscriber);
    }

    public Player getPlayerToMove() {
        return players.get(playerToMoveIndex);
    }

    public void addPlayer(Player player) throws GameFullException {
        if (isGameFull()) {
            throw new GameFullException();
        }
        players.add(player);
        if (isGameFull()) {
            gameEventPublisher.publishEvent(GameEventType.GAME_READY, new GameEvent(this));
        }
    }

    public boolean isGameFull() {
        return players.size() == maxNoOfPlayers;
    }

    private void validateMove(Player player, GameMove move) throws InvalidMoveException {
        try {
            if (!this.getPlayerToMove().equals(player)) {
                throw new InvalidMoveException("Not your turn. It's this player turn => " + player.getSymbol());
            }

            if (board.getPlayerAt(move.getX(), move.getY()) != null) {
                throw new InvalidMoveException("Not empty field");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidMoveException("Given position is outside of board");
        }
    }

    public void makeMove(Player player, GameMove move) throws InvalidMoveException {
        this.validateMove(player, move);
        board.setPlayerAt(move.getX(), move.getY(), player);
        playerToMoveIndex = (playerToMoveIndex+1) % players.size();

        Player winner = getWinner();

        // There's a winner or board is filled
        if (winner != null || board.isFilled()) {
            gameEventPublisher.publishEvent(GameEventType.GAME_ENDED, new GameEvent(this));
        } else {
            gameEventPublisher.publishEvent(GameEventType.PLAYER_MOVED, new GameEvent(this));
        }
    }

    public Player getWinner() {
        return winnerStrategy.getWinner(board, players);
    }
}
