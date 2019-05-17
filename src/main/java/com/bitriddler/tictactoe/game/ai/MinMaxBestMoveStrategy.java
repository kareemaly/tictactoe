package com.bitriddler.tictactoe.game.ai;

import com.bitriddler.tictactoe.game.GameBoard;
import com.bitriddler.tictactoe.game.GameMove;
import com.bitriddler.tictactoe.game.Player;

import java.util.List;
import java.util.stream.Collectors;

public class MinMaxBestMoveStrategy implements BestMoveStrategy {
    private Player[] humanPlayers;
    private Player aiPlayer;

    public MinMaxBestMoveStrategy(Player[] humanPlayers) {
        this.humanPlayers = humanPlayers;
    }

    public MinMaxBestMoveStrategy(Player[] humanPlayers, Player aiPlayer) {
        this.humanPlayers = humanPlayers;
        this.aiPlayer = aiPlayer;
    }

    public void setAiPlayer(Player aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    int getPlayerScore(GameBoard board, Player player) {
        // Rate of growth when player is close to win
        // e.g. player have 4 in a row will have score 2^5 = 32 only for that row
        int rate = 2;
        int count = 0;
        int size = board.size();
        int reverseDiagCount = 1;
        int diagCount = 1;
        boolean stopCountingReverseDiag = false;
        boolean stopCountingDiag = false;
        for (int i = 0; i < size; i++) {
            int rowCount = 1;
            int colCount = 1;
            boolean stopCountingRows = false;
            boolean stopCountingCols = false;
            for (int j = 0; j < size; j++) {
                if (!stopCountingRows && player.equals(board.getPlayerAt(i, j))) {
                    rowCount*=rate;
                }
                // There's another player in row
                else if (board.getPlayerAt(i, j) != null) {
                    rowCount = 1;
                    stopCountingRows = true;
                }

                if (!stopCountingCols && player.equals(board.getPlayerAt(j, i))) {
                    colCount*=rate;
                }
                // There's another player in col
                else if (board.getPlayerAt(j, i) != null) {
                    colCount = 1;
                    stopCountingCols = true;
                }
            }

            count += rowCount + colCount - 2;

            if (!stopCountingDiag && player.equals(board.getPlayerAt(i, i))) {
                diagCount*=rate;
            // There's another player in diag
            } else if (board.getPlayerAt(i, i) != null) {
                diagCount = 1;
                stopCountingDiag = true;
            }

            if (!stopCountingReverseDiag && player.equals(board.getPlayerAt(i, size-i-1))) {
                reverseDiagCount*=rate;
            // There's another player in reverse diag
            } else if (board.getPlayerAt(i, size-i-1) != null) {
                reverseDiagCount = 1;
                stopCountingReverseDiag = true;
            }
        }
        return count + reverseDiagCount + diagCount - 2;
    }

    int staticBoardEvaluation(GameBoard board) {
        int aiScore = getPlayerScore(board, aiPlayer);

        // Calculating total human score against the ai player
        // will make the ai player assume the worst which is, both humans
        // are playing to make him lose
        int totalHumanScore = 0;

        for (Player humanPlayer: humanPlayers) {
            totalHumanScore += getPlayerScore(board, humanPlayer);
        }

        return aiScore - totalHumanScore;
    }

    List<GameBoard> getAllPossiblePositions(Player player, GameBoard position) {
        return position.getAllAvailableMoves().stream().map(move -> {
            return position.applyMoveOnClone(move, player);
        }).collect(Collectors.toList());
    }

    public GameMove findBestMove(GameBoard position, int depth) {
        List<GameBoard> possiblePositions = this.getAllPossiblePositions(aiPlayer, position);
        int maxEval = Integer.MIN_VALUE;
        GameBoard bestPosition = null;

        for (GameBoard newPosition: possiblePositions) {
            int eval = minmax(newPosition, depth - 1, humanPlayers.length - 1);
            if (eval > maxEval) {
                maxEval = eval;
                bestPosition = newPosition;
            }
            maxEval = Math.max(maxEval, eval);
        }

        return position.getMoveToReach(bestPosition);
    }

    int minmax(GameBoard position, int depth, int humanPlayerNo) {
        // If depth is 0 or game is over
        if (depth == 0 || position.isFilled()) {
            return staticBoardEvaluation(position);
        }

        if (humanPlayerNo < 0) {
            List<GameBoard> possiblePositions = this.getAllPossiblePositions(aiPlayer, position);
            int maxEval = Integer.MIN_VALUE;
            for (GameBoard newPosition: possiblePositions) {
                int eval = minmax(newPosition, depth - 1, humanPlayers.length - 1);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            List<GameBoard> possiblePositions = this.getAllPossiblePositions(humanPlayers[humanPlayerNo], position);

            int minEval = Integer.MAX_VALUE;
            for (GameBoard newPosition: possiblePositions) {
                int eval = minmax(newPosition, depth - 1, humanPlayerNo - 1);
                minEval = Math.min(minEval, eval);
            }

            return minEval;
        }
    }
}
