package com.bitriddler.tictactoe;

import com.bitriddler.tictactoe.game.GameMove;
import com.bitriddler.tictactoe.game.TicTacToe;
import com.bitriddler.tictactoe.game.boardDisplay.BoardViewStrategy;
import com.bitriddler.tictactoe.game.events.GameEvent;
import com.bitriddler.tictactoe.game.events.GameEventType;
import com.bitriddler.tictactoe.game.exceptions.InvalidMoveCommandException;
import com.bitriddler.tictactoe.game.moveParser.MoveParserStrategy;
import com.bitriddler.tictactoe.game.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class SocketConnectionHandlerTest {
    @Mock
    ClientSocketConnection clientSocketConnection;
    @Mock
    BoardViewStrategy boardViewStrategy;
    @Mock
    MoveParserStrategy moveParserStrategy;
    @Mock
    TicTacToe game;
    @Mock
    Player player;
    @Mock
    Player p2;
    @Mock
    GameEvent event;
    @Mock
    GameMove move;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    SocketConnectionHandler createPlayerHandler() {
        return new SocketConnectionHandler(clientSocketConnection, game, player, moveParserStrategy, boardViewStrategy);
    }

    @Test
    void testEventsPrintingBoard() {
        SocketConnectionHandler playerHandler = this.createPlayerHandler();
        doReturn(p2).when(event).getPlayerToMove();
        doReturn("board view").when(boardViewStrategy).get(any());
        playerHandler.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(clientSocketConnection).output("board view");
    }

    @Test
    void testEventsPrintingWaitingForPlayerIfNotMyTurn() {
        SocketConnectionHandler playerHandler = this.createPlayerHandler();
        doReturn(p2).when(event).getPlayerToMove();
        playerHandler.handleEvent(GameEventType.PLAYER_MOVED, event);
        ArgumentCaptor<String> outputCaptor = ArgumentCaptor.forClass(String.class);

        verify(clientSocketConnection, times(2))
                .output(outputCaptor.capture());

        assertTrue(outputCaptor.getAllValues().get(1).startsWith("Waiting for player"));
    }

    @Test
    void testEventsPrintingYourTurn() {
        SocketConnectionHandler playerHandler = this.createPlayerHandler();
        doReturn(player).when(event).getPlayerToMove();
        playerHandler.handleEvent(GameEventType.PLAYER_MOVED, event);
        ArgumentCaptor<String> outputCaptor = ArgumentCaptor.forClass(String.class);
        verify(clientSocketConnection, times(2))
                .output(outputCaptor.capture());
        assertEquals("Your Turn...", outputCaptor.getAllValues().get(1));
    }

    @Test
    void testEventsAskingForInputIfMyTurn() {
        SocketConnectionHandler playerHandler = this.createPlayerHandler();
        doReturn(player).when(event).getPlayerToMove();
        playerHandler.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(clientSocketConnection, times(1)).input();
    }

    @Test
    void testParserIsCalledWithUserInput() throws Exception {
        SocketConnectionHandler playerHandler = this.createPlayerHandler();
        doReturn(player).when(event).getPlayerToMove();
        doReturn("Something").when(clientSocketConnection).input();
        playerHandler.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(moveParserStrategy, times(1)).parse("Something");
    }

    @Test
    void testMoveIsMade() throws Exception {
        SocketConnectionHandler playerHandler = this.createPlayerHandler();
        doReturn(player).when(event).getPlayerToMove();
        doReturn(move).when(moveParserStrategy).parse(any());
        playerHandler.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(event).makeMove(player, move);
    }

    @Test
    void testHandlingInvalidCommandException() throws Exception {
        SocketConnectionHandler playerHandler = this.createPlayerHandler();
        doReturn(player).when(event).getPlayerToMove();
        doThrow(InvalidMoveCommandException.class)
                .doReturn(move)
                .when(moveParserStrategy)
                .parse(any());
        playerHandler.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(clientSocketConnection).output("Command must be in this format: `x,y`");
        verify(clientSocketConnection, times(2)).input();
    }

    @Test
    void testHandlingInvalidMoveException() throws Exception {
        SocketConnectionHandler playerHandler = this.createPlayerHandler();
        doReturn(player).when(event).getPlayerToMove();
        doThrow(InvalidMoveCommandException.class)
                .doReturn(move)
                .when(moveParserStrategy)
                .parse(any());
        playerHandler.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(clientSocketConnection, times(2)).input();
    }

    @Test
    void testRepeatingInputUntilValid() throws Exception {
        SocketConnectionHandler playerHandler = this.createPlayerHandler();
        doReturn(player).when(event).getPlayerToMove();
        doThrow(InvalidMoveCommandException.class)
                .doThrow(InvalidMoveCommandException.class)
                .doThrow(InvalidMoveCommandException.class)
                .doThrow(InvalidMoveCommandException.class)
                .doReturn(move)
                .when(moveParserStrategy)
                .parse(any());
        playerHandler.handleEvent(GameEventType.PLAYER_MOVED, event);
        verify(clientSocketConnection, times(5)).input();
    }

}