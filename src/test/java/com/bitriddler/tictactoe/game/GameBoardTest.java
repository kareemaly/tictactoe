package com.bitriddler.tictactoe.game;

import static org.mockito.Mockito.*;

import com.bitriddler.tictactoe.game.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    @Mock
    Player p1;

    @Mock
    GameMove gameMove;

    private GameBoard createBoard(int size) {
        return new GameBoard(size);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testInitializeBoardWithGivenSize() {
        GameBoard board = this.createBoard(3);
        assertEquals(3, board.size());
    }

    @Test
    void testInitializeBoardWithNullValues() {
        GameBoard board = this.createBoard(2);
        assertEquals(null, board.getPlayerAt(0, 0));
        assertEquals(null, board.getPlayerAt(0, 1));
        assertEquals(null, board.getPlayerAt(1, 0));
        assertEquals(null, board.getPlayerAt(1, 1));
    }

    @Test
    void testSetAndGetPlayerAt() {
        GameBoard board = this.createBoard(3);
        board.setPlayerAt(0, 1, p1);
        assertEquals(p1, board.getPlayerAt(0, 1));
    }

    @Test
    void testThrowArrayIndexOutOfBounds() {
        GameBoard board = this.createBoard(3);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.setPlayerAt(3, 1, p1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> board.getPlayerAt(1, 5));
    }

    @Test
    void testApplyMove() {
        GameBoard board = this.createBoard(3);
        doReturn(1).when(gameMove).getX();
        doReturn(2).when(gameMove).getY();
        assertNotEquals(p1, board.getPlayerAt(1, 2));
        board.applyMove(gameMove, p1);
        assertEquals(p1, board.getPlayerAt(1, 2));
    }

    @Test
    void testApplyMoveOnClone() {
        GameBoard board = this.createBoard(3);
        doReturn(1).when(gameMove).getX();
        doReturn(2).when(gameMove).getY();
        GameBoard newBoard = board.applyMoveOnClone(gameMove, p1);
        assertNotEquals(p1, board.getPlayerAt(1, 2));
        assertEquals(p1, newBoard.getPlayerAt(1, 2));
        assertNotEquals(board, newBoard);
    }

    @Test
    void testGetAllAvailableMoves() {
        GameBoard board = this.createBoard(3);
        board.setPlayerAt(0,0, p1);
        board.setPlayerAt(1,1, p1);
        board.setPlayerAt(2,2, p1);
        ArrayList<GameMove> actualMoves = board.getAllAvailableMoves();
        GameMove[] expectedMoves = new GameMove[]{
                new GameMove(0, 1),
                new GameMove(0, 2),
                new GameMove(1, 0),
                new GameMove(1, 2),
                new GameMove(2, 0),
                new GameMove(2, 1),
        };
        assertArrayEquals(
                Arrays.stream(expectedMoves).mapToInt(move -> move.getX()).toArray(),
                actualMoves.stream().mapToInt(move -> move.getX()).toArray()
        );
        assertArrayEquals(
                Arrays.stream(expectedMoves).mapToInt(move -> move.getY()).toArray(),
                actualMoves.stream().mapToInt(move -> move.getY()).toArray()
        );
    }

    @Test
    void testBoardIsFilled() {
        GameBoard board = this.createBoard(2);
        board.setPlayerAt(0, 0, p1);
        assertFalse(board.isFilled());
        board.setPlayerAt(0, 1, p1);
        board.setPlayerAt(1, 0, p1);
        board.setPlayerAt(1, 1, p1);
        assertTrue(board.isFilled());
    }

    @Test
    void testBoardIsEmpty() {
        GameBoard board = this.createBoard(2);
        assertTrue(board.isEmpty());
        board.setPlayerAt(1, 1, p1);
        assertFalse(board.isEmpty());
    }

    @Test
    void testGetMoveToReachNewBoardPosition() {
        GameBoard b1 = this.createBoard(2);
        GameBoard b2 = this.createBoard(2);
        b1.setPlayerAt(0, 0, p1);
        b1.setPlayerAt(0, 1, p1);
        b2.setPlayerAt(0, 0, p1);
        b2.setPlayerAt(0, 1, p1);
        b2.setPlayerAt(1, 1, p1);
        GameMove move = b1.getMoveToReach(b2);
        assertEquals(move.getX(), 1);
        assertEquals(move.getY(), 1);
    }
}