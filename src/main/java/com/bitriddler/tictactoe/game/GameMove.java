package com.bitriddler.tictactoe.game;

public class GameMove {
    private int x;
    private int y;

    public GameMove(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
