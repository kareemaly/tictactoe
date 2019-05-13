package com.bitriddler.tictactoe.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Player getDummyPlayer() {
        return new Player();
    }

    @Test
    public void testBoardSizeInitializedSameAsGiven() throws Exception {
        Board board = new Board(9);
        assertEquals(9, board.getBoard().length);
    }

    @Test
    public void testThrowsBoardSizeInvalidIfBoardSizeNotInRange() {
        assertThrows(BoardSizeInvalidException.class, () -> new Board(11));
        assertThrows(BoardSizeInvalidException.class, () -> new Board(2));
    }

    @Test
    public void testAllBoardInitializedWithInitialValue() throws Exception {
        Board board = new Board(3);
        int[][] expectedBoard = {
                { -1, -1, -1 },
                { -1, -1, -1 },
                { -1, -1, -1 }
        };
        assertArrayEquals(expectedBoard, board.getBoard());
    }

    @Test
    public void testPlayerAddedToPlayersArrayList() throws Exception {
        Board board = new Board(3);
        Player p1 = this.getDummyPlayer();
        Player p2 = this.getDummyPlayer();
        board.addPlayer(p1);
        board.addPlayer(p2);
        assertEquals(p1, board.getPlayers().get(0));
        assertEquals(p2, board.getPlayers().get(1));
        assertEquals(2, board.getPlayers().size());
    }

    @Test
    public void testThrowsGameFullExceptionIfPlayersGreaterThan3() throws Exception {
        Board board = new Board(3);
        Player p1 = this.getDummyPlayer();
        Player p2 = this.getDummyPlayer();
        Player p3 = this.getDummyPlayer();
        Player p4 = this.getDummyPlayer();
        board.addPlayer(p1);
        board.addPlayer(p2);
        board.addPlayer(p3);
        assertThrows(GameFullException.class, () -> board.addPlayer(p4));
    }

    @Test
    public void testGivenBoardPositionIsFilledWithCorrectPlayerNumber() throws Exception {
        Board board = new Board(3);
        Player p1 = this.getDummyPlayer();
        Player p2 = this.getDummyPlayer();
        board.addPlayer(p1);
        board.addPlayer(p2);
        board.makeMove(p1, 1, 1);
        board.makeMove(p2, 1, 2);
        assertEquals(0, board.getBoard()[1][1]);
        assertEquals(1, board.getBoard()[1][2]);
    }

    @Test
    public void testInvalidMoveExceptionThrownIfIndexOutOfBounds() throws Exception {
        Board board = new Board(3);
        Player p1 = this.getDummyPlayer();
        board.addPlayer(p1);
        assertThrows(InvalidMoveException.class, () -> board.makeMove(p1, 3, 1));
        assertThrows(InvalidMoveException.class, () -> board.makeMove(p1, 2, 3));
    }

    @Test
    public void testInvalidMoveExceptionThrownIfPositionNotInitialValue() throws Exception {
        Board board = new Board(3);
        Player p1 = this.getDummyPlayer();
        board.addPlayer(p1);
        board.makeMove(p1, 0, 1);
        board.makeMove(p1, 1, 1);
        assertThrows(InvalidMoveException.class, () -> board.makeMove(p1, 0, 1));
        assertThrows(InvalidMoveException.class, () -> board.makeMove(p1, 1, 1));
    }

    @Test
    public void testNoWinnerIfIncorrectPattern() throws Exception {
        Board board = new Board(3);
        Player p1 = this.getDummyPlayer();
        Player p2 = this.getDummyPlayer();
        board.addPlayer(p1);
        board.addPlayer(p2);
        board.makeMove(p1, 0, 0);
        board.makeMove(p1, 0, 1);
        board.makeMove(p2, 0, 2);
        board.makeMove(p1, 1, 1);
        board.makeMove(p1, 2, 0);
        assertEquals(null, board.getWinner());
    }

    @Test
    public void testGetWinnerInCaseOfRows() throws Exception {
        Board board = new Board(3);
        Player p1 = this.getDummyPlayer();
        board.addPlayer(p1);
        board.makeMove(p1, 0, 0);
        board.makeMove(p1, 0, 1);
        board.makeMove(p1, 0, 2);
        assertEquals(p1, board.getWinner());

        board = new Board(4);
        board.addPlayer(p1);
        board.makeMove(p1, 2, 0);
        board.makeMove(p1, 2, 1);
        board.makeMove(p1, 2, 2);
        board.makeMove(p1, 2, 3);
        assertEquals(p1, board.getWinner());
    }

    @Test
    public void testGetWinnerInCaseOfColumns() throws Exception {
        Board board = new Board(3);
        Player p1 = this.getDummyPlayer();
        board.addPlayer(p1);
        board.makeMove(p1, 0, 1);
        board.makeMove(p1, 1, 1);
        board.makeMove(p1, 2, 1);
        assertEquals(p1, board.getWinner());
    }

    @Test
    public void testGetWinnerInCaseOfDiagonals() throws Exception {
        Board board = new Board(4);
        Player p1 = this.getDummyPlayer();
        board.addPlayer(p1);
        board.makeMove(p1, 0, 0);
        board.makeMove(p1, 1, 1);
        board.makeMove(p1, 2, 2);
        board.makeMove(p1, 3, 3);
        assertEquals(p1, board.getWinner());

        // reverse diagonal
        board = new Board(3);
        board.addPlayer(p1);
        board.makeMove(p1, 0, 2);
        board.makeMove(p1, 1, 1);
        board.makeMove(p1, 2, 0);
        assertEquals(p1, board.getWinner());
    }
}