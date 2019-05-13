package com.bitriddler.tictactoe.game;

import java.util.ArrayList;

public class Board {
    static final int INITIAL_VALUE = -1;
    private ArrayList<Player> players = new ArrayList<>();
    private int[][] board;

    public Board(int boardSize) throws BoardSizeInvalidException {
        if (boardSize > 10 || boardSize < 3) {
            throw new BoardSizeInvalidException();
        }
        this.board = new int[boardSize][boardSize];
        this.initialize();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int[][] getBoard() {
        return board;
    }

    private void initialize() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = INITIAL_VALUE;
            }
        }
    }

    public void addPlayer(Player player) throws GameFullException {
        if (players.size() > 2) {
            throw new GameFullException();
        }
        players.add(player);
    }

    public void makeMove(Player player, int x, int y) throws InvalidMoveException {
        try {
            if (board[x][y] != INITIAL_VALUE) {
                throw new InvalidMoveException();
            }
            board[x][y] = players.indexOf(player);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidMoveException();
        }
    }

    public Player getWinner() {
        int countRow = 0;
        int countCol = 0;
        int countDiag = 0;
        int countReverseDiag = 0;
        int size = board.length;

        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    if (board[j][k] == i) {
                        countRow++;
                    }

                    if (board[k][j] == i) {
                        countCol++;
                    }
                }

                if (board[j][j] == i) {
                    countDiag++;
                }

                if (board[j][size-j-1] == i) {
                    countReverseDiag++;
                }

                if (
                    countDiag == size ||
                    countCol == size ||
                    countRow == size ||
                    countReverseDiag == size
                ) {
                    return players.get(i);
                }

                countCol = 0;
                countRow = 0;
            }
            countDiag = 0;
            countReverseDiag = 0;
        }

        return null;
    }
}
