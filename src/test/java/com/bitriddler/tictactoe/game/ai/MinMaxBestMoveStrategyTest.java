package com.bitriddler.tictactoe.game.ai;

import static org.mockito.Mockito.*;

import com.bitriddler.tictactoe.game.GameBoard;
import com.bitriddler.tictactoe.game.GameMove;
import com.bitriddler.tictactoe.game.Player;
import com.bitriddler.tictactoe.game.winner.WinnerStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class MinMaxBestMoveStrategyTest {
    @Mock
    Player p1;
    @Mock
    Player p2;
    @Mock
    Player ai;
    @Mock
    WinnerStrategy winnerStrategy;

    MinMaxBestMoveStrategy createInstance() {
        return spy(new MinMaxBestMoveStrategy(winnerStrategy, new Player[]{p1, p2}, ai));
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGettingPlayerScoreWith3x3() {
        GameBoard board = new GameBoard(3);
        MinMaxBestMoveStrategy strategy = createInstance();

        board.setPlayerAt(1, 1, p1);
        assertEquals(4, strategy
                .getPlayerScore(board, p1));

        board.setPlayerAt(0, 0, p2);
        assertEquals(3, strategy
                .getPlayerScore(board, p1));
        assertEquals(2, strategy
                .getPlayerScore(board, p2));

        board.setPlayerAt(2, 2, ai);
        board.setPlayerAt(1, 2, ai);
        assertEquals(2, strategy
                .getPlayerScore(board, p1));
        assertEquals(2, strategy
                .getPlayerScore(board, p2));
        assertEquals(3, strategy
                .getPlayerScore(board, ai));
    }

    @Test
    void testGettingPlayerScoreWith5x5() {
        GameBoard board = new GameBoard(5);
        MinMaxBestMoveStrategy strategy = createInstance();

        board.setPlayerAt(2, 2, p1);
        assertEquals(4, strategy
                .getPlayerScore(board, p1));

        board.setPlayerAt(1, 1, p2);
        assertEquals(3, strategy
                .getPlayerScore(board, p1));
        assertEquals(2, strategy
                .getPlayerScore(board, p2));

        board.setPlayerAt(1, 2, p2);
        assertEquals(2, strategy
                .getPlayerScore(board, p1));
        assertEquals(3, strategy
                .getPlayerScore(board, p2));

        board.setPlayerAt(1, 3, p2);
        assertEquals(1, strategy
                .getPlayerScore(board, p1));
        assertEquals(5, strategy
                .getPlayerScore(board, p2));

        // p1 closed the row
        board.setPlayerAt(1, 4, p1);
        assertEquals(2, strategy
                .getPlayerScore(board, p1));
        assertEquals(2, strategy
                .getPlayerScore(board, p2));
    }

    @Test
    void testGettingMaximumScoreInCaseOfColWin() {
        GameBoard board = new GameBoard(3);
        MinMaxBestMoveStrategy strategy = createInstance();

        board.setPlayerAt(0, 2, p1);
        board.setPlayerAt(1, 2, p1);
        board.setPlayerAt(2, 2, p1);
        assertEquals(Integer.MAX_VALUE, strategy
                .getPlayerScore(board, p1));
    }

    @Test
    void testGettingMaximumScoreInCaseOfRowWin() {
        GameBoard board = new GameBoard(3);
        MinMaxBestMoveStrategy strategy = createInstance();

        board.setPlayerAt(1, 0, p1);
        board.setPlayerAt(1, 1, p1);
        board.setPlayerAt(1, 2, p1);
        assertEquals(Integer.MAX_VALUE, strategy
                .getPlayerScore(board, p1));
    }

    @Test
    void testGettingMaximumScoreInCaseOfDiagWin() {
        GameBoard board = new GameBoard(3);
        MinMaxBestMoveStrategy strategy = createInstance();

        board.setPlayerAt(0, 0, p1);
        board.setPlayerAt(1, 1, p1);
        board.setPlayerAt(2, 2, p1);
        assertEquals(Integer.MAX_VALUE, strategy
                .getPlayerScore(board, p1));
    }

    @Test
    void testGettingMaximumScoreInCaseOfReverseDiagWin() {
        GameBoard board = new GameBoard(3);
        MinMaxBestMoveStrategy strategy = createInstance();

        board.setPlayerAt(0, 2, p1);
        board.setPlayerAt(1, 1, p1);
        board.setPlayerAt(2, 0, p1);
        assertEquals(Integer.MAX_VALUE, strategy
                .getPlayerScore(board, p1));
    }

    @Test
    void testEvaluationGettingAllPlayersScores() {
        MinMaxBestMoveStrategy strategy = createInstance();
        strategy.staticBoardEvaluation(new GameBoard(3));
        verify(strategy, times(3)).getPlayerScore(any(), any());
    }

    @Test
    void testEvaluationSubtractingAiScoreFromHumanScore() {
        MinMaxBestMoveStrategy strategy = createInstance();
        doReturn(3)
                .doReturn(4)
                .doReturn(5)
                .when(strategy).getPlayerScore(any(), any());
        int value = strategy.staticBoardEvaluation(new GameBoard(3));
        assertEquals(3 - (4 + 5), value);
    }

    @Test
    void testFindBestMoveIsNotAtLeastStupid() {
        MinMaxBestMoveStrategy strategy = createInstance();
        GameBoard board = new GameBoard(3);
        board.setPlayerAt(0, 0, p1);
        board.setPlayerAt(0, 1, p1);
        // Best move obviously is 0,2
        GameMove bestMove = strategy.findBestMove(board, 5);
        assertEquals(0, bestMove.getX());
        assertEquals(2, bestMove.getY());

        board.setPlayerAt(0, 2, ai);
        board.setPlayerAt(2, 2, ai);

        // Best move to win is 1,2
        bestMove = strategy.findBestMove(board, 5);
        assertEquals(1, bestMove.getX());
        assertEquals(2, bestMove.getY());

        board.setPlayerAt(1, 2, p2);
        board.setPlayerAt(1, 0, p2);

        // Best move to not lose from p2 is 1,1
        bestMove = strategy.findBestMove(board, 5);
        assertEquals(1, bestMove.getX());
        assertEquals(1, bestMove.getY());
    }
}