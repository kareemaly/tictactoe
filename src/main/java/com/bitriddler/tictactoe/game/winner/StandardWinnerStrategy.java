package com.bitriddler.tictactoe.game.winner;

import com.bitriddler.tictactoe.game.GameBoard;
import com.bitriddler.tictactoe.game.Player;

public class StandardWinnerStrategy implements WinnerStrategy {
    @Override
    public Player getWinner(GameBoard board, Player[] players) {
        int countRow = 0;
        int countCol = 0;
        int countDiag = 0;
        int countReverseDiag = 0;
        int size = board.size();

        for (Player player: players) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    if (player.equals(board.getPlayerAt(j, k))) {
                        countRow++;
                    }

                    if (player.equals(board.getPlayerAt(k, j))) {
                        countCol++;
                    }
                }

                if (player.equals(board.getPlayerAt(j, j))) {
                    countDiag++;
                }

                if (player.equals(board.getPlayerAt(j, size-j-1))) {
                    countReverseDiag++;
                }

                if (
                        countDiag == size ||
                        countCol == size ||
                        countRow == size ||
                        countReverseDiag == size
                ) {
                    return player;
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
