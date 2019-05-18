package com.bitriddler.tictactoe.game;

import com.bitriddler.tictactoe.game.boardDisplay.BoardViewStrategy;
import com.bitriddler.tictactoe.game.events.GameEvent;
import com.bitriddler.tictactoe.game.events.GameEventType;
import com.bitriddler.tictactoe.game.exceptions.InvalidMoveCommandException;
import com.bitriddler.tictactoe.game.moveParser.MoveParserStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class HumanPlayerTest {
    @Mock
    GameIO gameIO;
    @Mock
    BoardViewStrategy boardViewStrategy;
    @Mock
    MoveParserStrategy moveParserStrategy;
    @Mock
    GameEvent event;
    @Mock
    GameMove move;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    HumanPlayer createInstance(char symbol) {
        return new HumanPlayer(symbol, gameIO, boardViewStrategy, moveParserStrategy);
    }

    @Test
    void testEventsPrintingBoard() {
        HumanPlayer humanPlayer = this.createInstance('s');
        HumanPlayer opponent = this.createInstance('o');
        doReturn(opponent).when(event).getPlayerToMove();
        doReturn("board view").when(boardViewStrategy).get();
        humanPlayer.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(gameIO).output("board view");
    }

    @Test
    void testEventsPrintingWaitingForPlayerIfNotMyTurn() {
        HumanPlayer humanPlayer = this.createInstance('s');
        HumanPlayer opponent = this.createInstance('o');
        doReturn(opponent).when(event).getPlayerToMove();
        humanPlayer.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(gameIO).output("Waiting for player (o) to move");
    }

    @Test
    void testEventsPrintingYourTurn() {
        HumanPlayer humanPlayer = this.createInstance('s');
        doReturn(humanPlayer).when(event).getPlayerToMove();
        humanPlayer.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(gameIO).output("Your Turn...");
    }

    @Test
    void testEventsAskingForInputIfMyTurn() {
        HumanPlayer humanPlayer = this.createInstance('s');
        doReturn(humanPlayer).when(event).getPlayerToMove();
        humanPlayer.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(gameIO, times(1)).input();
    }

    @Test
    void testParserIsCalledWithUserInput() throws Exception {
        HumanPlayer humanPlayer = this.createInstance('s');
        doReturn(humanPlayer).when(event).getPlayerToMove();
        doReturn("Something").when(gameIO).input();
        humanPlayer.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(moveParserStrategy, times(1)).parse("Something");
    }

    @Test
    void testMoveIsMade() throws Exception {
        HumanPlayer humanPlayer = this.createInstance('s');
        doReturn(humanPlayer).when(event).getPlayerToMove();
        doReturn(move).when(moveParserStrategy).parse(any());
        humanPlayer.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(event).makeMove(humanPlayer, move);
    }

    @Test
    void testHandlingInvalidCommandException() throws Exception {
        HumanPlayer humanPlayer = this.createInstance('s');
        doReturn(humanPlayer).when(event).getPlayerToMove();
        doThrow(InvalidMoveCommandException.class)
                .doReturn(move)
                .when(moveParserStrategy)
                .parse(any());
        humanPlayer.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(gameIO).output("Command must be in this format: `x,y`");
        verify(gameIO, times(2)).input();
    }

    @Test
    void testHandlingInvalidMoveException() throws Exception {
        HumanPlayer humanPlayer = this.createInstance('s');
        doReturn(humanPlayer).when(event).getPlayerToMove();
        doThrow(InvalidMoveCommandException.class)
                .doReturn(move)
                .when(moveParserStrategy)
                .parse(any());
        humanPlayer.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(gameIO, times(2)).input();
    }

    @Test
    void testRepeatingInputUntilValid() throws Exception {
        HumanPlayer humanPlayer = this.createInstance('s');
        doReturn(humanPlayer).when(event).getPlayerToMove();
        doThrow(InvalidMoveCommandException.class)
                .doThrow(InvalidMoveCommandException.class)
                .doThrow(InvalidMoveCommandException.class)
                .doThrow(InvalidMoveCommandException.class)
                .doReturn(move)
                .when(moveParserStrategy)
                .parse(any());
        humanPlayer.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(gameIO, times(5)).input();
    }
}