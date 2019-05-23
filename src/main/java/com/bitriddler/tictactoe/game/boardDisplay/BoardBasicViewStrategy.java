package com.bitriddler.tictactoe.game.boardDisplay;

import com.bitriddler.tictactoe.game.GameBoard;
import com.bitriddler.tictactoe.game.players.Player;

public class BoardBasicViewStrategy implements BoardViewStrategy {

    private String getRowString(GameBoard board, int rowNo) {
        String result = "";
        for (int j = 0; j < board.size(); j++) {
            if (j == 0) {
                result += "| ";
            }
            Player player = board.getPlayerAt(rowNo, j);

            if (player != null) {
                result += player.getSymbol() + " | ";
            } else {
                result += "  | ";
            }
        }
        return result;
    }

    private String getLineSeparator(GameBoard board) {
        return "\n" + String.format(
                String.format("%%%ds", board.size()*4+1),
                " "
        ).replace(" ","-") + "\n";
    }

    @Override
    public String get(GameBoard board) {
        String result = "";
        int size = board.size();
        for (int i = 0; i < size; i++) {
            result += this.getRowString(board, i);
            if (i != size - 1) {
                result += this.getLineSeparator(board);
            }
        }
        return result;
    }
}
