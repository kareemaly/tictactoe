package com.bitriddler.tictactoe.game.players;

public class PlayerFactory {
    public Player makeHumanPlayer(char symbol) {
        return new HumanPlayer(symbol);
    }
    public Player makeAiPlayer(char symbol) {
        return new AiPlayer(symbol);
    }
}
