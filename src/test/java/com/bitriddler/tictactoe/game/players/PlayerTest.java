package com.bitriddler.tictactoe.game.players;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DummyPlayer extends Player {

    public DummyPlayer(char symbol) {
        super(symbol);
    }
}

class PlayerTest {
    Player createPlayer(char symbol) {
        return spy(new DummyPlayer(symbol));
    }

    @Test
    void testGetSymbol() {
        Player player = this.createPlayer('s');
        assertEquals('s', player.getSymbol());
    }

}