package com.bitriddler.tictactoe.game;

public class BoardDisplay {
    private Board board;

    public BoardDisplay(Board board) {
        this.board = board;
    }

    private String getRowString(int rowNo) {
        String result = "";
        for (int j = 0; j < board.getBoardSize(); j++) {
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

    private String getLineSeparator() {
        return "\n" + String.format(
                String.format("%%%ds", board.getBoardSize()*4+1),
                " "
        ).replace(" ","-") + "\n";
    }

    @Override
    public String toString() {
        String result = "";
        int size = board.getBoardSize();
        for (int i = 0; i < size; i++) {
            result += this.getRowString(i);
            if (i != size - 1) {
                result += this.getLineSeparator();
            }
        }
        return result;
    } 
}
