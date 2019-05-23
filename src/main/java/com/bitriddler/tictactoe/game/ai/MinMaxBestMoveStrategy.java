package com.bitriddler.tictactoe.game.ai;

import com.bitriddler.tictactoe.game.GameBoard;
import com.bitriddler.tictactoe.game.GameMove;
import com.bitriddler.tictactoe.game.players.Player;
import com.bitriddler.tictactoe.game.winner.WinnerStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class MinMaxBestMoveStrategy implements BestMoveStrategy {
    private WinnerStrategy winnerStrategy;

    public MinMaxBestMoveStrategy(WinnerStrategy winnerStrategy) {
        this.winnerStrategy = winnerStrategy;
    }

    int getPlayerScore(GameBoard board, Player player) {
        // Rate of growth when player is close to win
        // e.g. player have 4 in a row will have score 2^5 = 32 only for that row
        int count = 0;
        int size = board.size();
        int reverseDiagCount = 0;
        int diagCount = 0;
        boolean stopCountingReverseDiag = false;
        boolean stopCountingDiag = false;
        for (int i = 0; i < size; i++) {
            int rowCount = 0;
            int colCount = 0;
            boolean stopCountingRows = false;
            boolean stopCountingCols = false;
            for (int j = 0; j < size; j++) {
                if (!stopCountingRows && player.equals(board.getPlayerAt(i, j))) {
                    rowCount++;
                    // Going to win, return maximum value
                    if (rowCount == size)
                        return Integer.MAX_VALUE;
                }
                // There's another player in row
                else if (board.getPlayerAt(i, j) != null) {
                    rowCount = 0;
                    stopCountingRows = true;
                }

                if (!stopCountingCols && player.equals(board.getPlayerAt(j, i))) {
                    colCount++;
                    // Going to win, return maximum value
                    if (colCount == size)
                        return Integer.MAX_VALUE;
                }
                // There's another player in col
                else if (board.getPlayerAt(j, i) != null) {
                    colCount = 0;
                    stopCountingCols = true;
                }
            }

            count += rowCount + colCount;

            if (!stopCountingDiag && player.equals(board.getPlayerAt(i, i))) {
                diagCount++;
                // Going to win, return maximum value
                if (diagCount == size)
                    return Integer.MAX_VALUE;
            // There's another player in diag
            } else if (board.getPlayerAt(i, i) != null) {
                diagCount = 0;
                stopCountingDiag = true;
            }

            if (!stopCountingReverseDiag && player.equals(board.getPlayerAt(i, size-i-1))) {
                reverseDiagCount++;
                // Going to win, return maximum value
                if (reverseDiagCount == size)
                    return Integer.MAX_VALUE;
            // There's another player in reverse diag
            } else if (board.getPlayerAt(i, size-i-1) != null) {
                reverseDiagCount = 0;
                stopCountingReverseDiag = true;
            }
        }
        return count + reverseDiagCount + diagCount;
    }

    int staticBoardEvaluation(Player[] opponentPlayers, Player player, GameBoard board) {
        int playerScore = getPlayerScore(board, player);

        // Calculating total human score against the ai player
        // will make the ai player assume the worst which is, both humans
        // are playing to make him lose
        int totalOpponentScore = 0;

        for (Player opponentPlayer: opponentPlayers) {
            totalOpponentScore += getPlayerScore(board, opponentPlayer);
        }

        return playerScore - totalOpponentScore;
    }

    List<GameBoard> getAllPossiblePositions(Player player, GameBoard position) {
        return position.getAllAvailableMoves().stream().map(move -> {
            return position.applyMoveOnClone(move, player);
        }).collect(Collectors.toList());
    }

    boolean isGameFinished(GameBoard position, Player player) {
        return winnerStrategy.getWinner(position, new Player[]{player}) != null;
    }

    public GameMove findBestMove(Player[] opponentPlayers, Player player, GameBoard position, int depth) {
        List<GameBoard> possiblePositions = this.getAllPossiblePositions(player, position);
        int maxEval = Integer.MIN_VALUE;
        GameBoard bestPosition = null;

        for (GameBoard newPosition: possiblePositions) {
            int eval = minmax(opponentPlayers, player, newPosition, depth - 1, opponentPlayers.length - 1, player);
            if (eval > maxEval) {
                maxEval = eval;
                bestPosition = newPosition;
            }
            maxEval = Math.max(maxEval, eval);
        }

        return position.getMoveToReach(bestPosition);
    }

    int minmax(Player[] opponentPlayers, Player player, GameBoard position, int depth, int opponentPlayerNo, Player lastPlayerToPlay) {
        // If depth is 0 or game is over
        if (depth == 0 || position.isFilled() || isGameFinished(position, lastPlayerToPlay)) {
            return staticBoardEvaluation(opponentPlayers, player, position);
        }

        if (opponentPlayerNo < 0) {
            List<GameBoard> possiblePositions = this.getAllPossiblePositions(player, position);
            int maxEval = Integer.MIN_VALUE;
            for (GameBoard newPosition: possiblePositions) {
                int eval = minmax(
                        opponentPlayers,
                        player,
                        newPosition,
                        depth - 1,
                        opponentPlayers.length - 1,
                        player
                );
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            List<GameBoard> possiblePositions = this.getAllPossiblePositions(opponentPlayers[opponentPlayerNo], position);

            int minEval = Integer.MAX_VALUE;
            for (GameBoard newPosition: possiblePositions) {
                int eval = minmax(
                        opponentPlayers,
                        player,
                        newPosition,
                        depth - 1,
                        opponentPlayerNo - 1,
                        opponentPlayers[opponentPlayerNo]
                );
                minEval = Math.min(minEval, eval);
            }

            return minEval;
        }
    }
}
