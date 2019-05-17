package com.bitriddler.tictactoe.game;

import java.util.ArrayList;

public class GameBoard {
    private Player[][] board;

    public GameBoard(int size) {
        this.board = new Player[size][size];
    }

    public GameBoard(Player[][] board) {
        this.board = board;
    }

    public Player getPlayerAt(int x, int y) {
        return board[x][y];
    }

    public int size() {
        return board.length;
    }

    public void setPlayerAt(int x, int y, Player player) {
        board[x][y] = player;
    }

    public void applyMove(GameMove move, Player player) {
        this.setPlayerAt(move.getX(), move.getY(), player);
    }

    public GameBoard applyMoveOnClone(GameMove move, Player player) {
        GameBoard cloned = this.cloneBoard();
        cloned.applyMove(move, player);
        return cloned;
    }

    public ArrayList<GameMove> getAllAvailableMoves() {
        ArrayList<GameMove> moves = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null) {
                    moves.add(new GameMove(i, j));
                }
            }
        }
        return moves;
    }

    public boolean isFilled() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isEmpty() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public GameMove getMoveToReach(GameBoard newBoard) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (getPlayerAt(i, j) != newBoard.getPlayerAt(i, j)) {
                    return new GameMove(i, j);
                }
            }
        }

        return null;
    }

    private GameBoard cloneBoard()
    {
        Player[][] newBoard = new Player[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return new GameBoard(newBoard);
    }
}
