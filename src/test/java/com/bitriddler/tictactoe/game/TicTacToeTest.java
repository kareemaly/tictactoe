package com.bitriddler.tictactoe.game;

import com.bitriddler.tictactoe.game.exceptions.BoardSizeInvalidException;
import com.bitriddler.tictactoe.game.exceptions.GameFullException;
import com.bitriddler.tictactoe.game.exceptions.InvalidMoveException;
import com.bitriddler.tictactoe.game.players.Player;
import com.bitriddler.tictactoe.game.winner.WinnerStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeTest {
    @Mock
    WinnerStrategy winnerStrategy;
    @Mock
    Player p1;
    @Mock
    Player p2;
    @Mock
    Player p3;
    @Mock
    Player p4;
    @Mock
    GameMove gameMove;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private TicTacToe create(int boardSize) throws BoardSizeInvalidException {
        return new TicTacToe(boardSize, winnerStrategy);
    }

    @Test
    void testThrowingBoardSizeInvalid() {
        assertThrows(BoardSizeInvalidException.class, () -> create(2));
        assertThrows(BoardSizeInvalidException.class, () -> create(11));
        assertDoesNotThrow(() -> create(4));
    }

    @Test
    void testAddingPlayers() throws Exception {
        TicTacToe ticTacToe = create(3);
        ticTacToe.addPlayer(p1);
        assertEquals(1, ticTacToe.getNumberOfConnectedPlayers());
        ticTacToe.addPlayer(p2);
        assertEquals(2, ticTacToe.getNumberOfConnectedPlayers());
    }

    @Test
    void testThrowingGameFull() throws Exception {
        TicTacToe ticTacToe = create(3);
        ticTacToe.addPlayer(p1);
        ticTacToe.addPlayer(p2);
        ticTacToe.addPlayer(p3);
        assertThrows(GameFullException.class, () -> ticTacToe.addPlayer(p4));
        assertEquals(3, ticTacToe.getNumberOfConnectedPlayers());
    }

    @Test
    void testIsGameFull() throws Exception {
        TicTacToe ticTacToe = create(3);
        ticTacToe.addPlayer(p1);
        ticTacToe.addPlayer(p2);
        ticTacToe.addPlayer(p3);
        assertTrue(ticTacToe.isGameFull());
    }

    @Test
    void testFirstPlayerPlayFirst() throws Exception {
        TicTacToe ticTacToe = create(3);
        ticTacToe.addPlayer(p1);
        ticTacToe.addPlayer(p2);
        ticTacToe.addPlayer(p3);
        assertEquals(p1, ticTacToe.getPlayerToMove());
    }

    @Test
    void testThrowingInvalidMoveIfNotYourTurn() throws Exception {
        TicTacToe ticTacToe = create(3);
        ticTacToe.addPlayer(p1);
        ticTacToe.addPlayer(p2);
        ticTacToe.addPlayer(p3);
        assertThrows(InvalidMoveException.class, () -> ticTacToe.makeMove(p2, gameMove));
        assertThrows(InvalidMoveException.class, () -> ticTacToe.makeMove(p3, gameMove));
    }

    @Test
    void testThrowingInvalidMoveIfPositionNotEmpty() throws Exception {
        TicTacToe ticTacToe = create(3);
        ticTacToe.addPlayer(p1);
        ticTacToe.addPlayer(p2);
        ticTacToe.addPlayer(p3);
        ticTacToe.makeMove(p1, new GameMove(0, 2));
        assertThrows(InvalidMoveException.class, () -> ticTacToe.makeMove(p2, new GameMove(0, 2)));
    }

    @Test
    void testThrowingInvalidMoveIfPositionOutsideBoard() throws Exception {
        TicTacToe ticTacToe = create(3);
        ticTacToe.addPlayer(p1);
        ticTacToe.addPlayer(p2);
        ticTacToe.addPlayer(p3);
        assertThrows(InvalidMoveException.class, () -> ticTacToe.makeMove(p1, new GameMove(3, 2)));
    }

    @Test
    void testAddingPlayerInBoard() throws Exception {
        TicTacToe ticTacToe = create(3);
        ticTacToe.addPlayer(p1);
        ticTacToe.addPlayer(p2);
        ticTacToe.addPlayer(p3);
        ticTacToe.makeMove(p1, new GameMove(0, 1));
        assertEquals(p1, ticTacToe.getBoard().getPlayerAt(0, 1));
    }

    @Test
    void testSwitchingToNextPlayer() throws Exception {
        TicTacToe ticTacToe = create(3);
        ticTacToe.addPlayer(p1);
        ticTacToe.addPlayer(p2);
        ticTacToe.addPlayer(p3);
        ticTacToe.makeMove(p1, new GameMove(0, 1));
        assertEquals(ticTacToe.getPlayerToMove(), p2);
        ticTacToe.makeMove(p2, new GameMove(0, 0));
        assertEquals(ticTacToe.getPlayerToMove(), p3);
        ticTacToe.makeMove(p3, new GameMove(1, 0));
        assertEquals(ticTacToe.getPlayerToMove(), p1);
    }

}