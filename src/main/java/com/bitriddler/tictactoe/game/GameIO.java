package com.bitriddler.tictactoe.game;

public interface GameIO {
    void output(String str);
    String input();
    void disconnect();
}
