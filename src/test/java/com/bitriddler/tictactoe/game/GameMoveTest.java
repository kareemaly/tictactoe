package com.bitriddler.tictactoe.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameMoveTest {
    @Test
    void testGettingX() {
        GameMove gameMove = new GameMove(10, 20);
        assertEquals(10, gameMove.getX());
    }
    @Test
    void testGettingY() {
        GameMove gameMove = new GameMove(10, 20);
        assertEquals(20, gameMove.getY());
    }
}