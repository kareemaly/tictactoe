package com.bitriddler.tictactoe.game.players;

import com.bitriddler.tictactoe.game.GameBoard;
import com.bitriddler.tictactoe.game.GameMove;
import com.bitriddler.tictactoe.game.ai.BestMoveStrategy;
import com.bitriddler.tictactoe.game.events.GameEvent;
import com.bitriddler.tictactoe.game.events.GameEventType;
import com.bitriddler.tictactoe.game.exceptions.InvalidMoveException;

import java.util.ArrayList;

public class AiPlayer extends Player {

    private BestMoveStrategy bestMoveStrategy;

    public AiPlayer(char symbol, BestMoveStrategy bestMoveStrategy) {
        super(symbol);
        this.bestMoveStrategy = bestMoveStrategy;
    }

    GameMove getRandomMove(GameBoard board) {
        ArrayList<GameMove> allMoves = board.getAllAvailableMoves();
        return allMoves
                .get((int) Math.floor(Math.random() * allMoves.size()));
    }

    void makeRandomMove(GameEvent event, boolean simulateThinking) {
        try {
            if (simulateThinking) {
                Thread.sleep(100);
            }
            event.makeMove(this, getRandomMove(event.getBoard()));
        } catch (InvalidMoveException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private int getBestDepth(int boardSize) {
        switch (boardSize) {
            case 3:
                return 15;
            case 4:
            case 5:
            case 6:
            case 7:
                return 4;
            case 8:
            case 9:
            case 10:
                return 3;
            default:
                return 4;
        }
    }

    private void handleGameStillOnEvents(GameEvent event) {
        if (this.equals(event.getPlayerToMove())) {
            // First move to make need to be random
            if (event.getBoard().isEmpty()) {
                makeRandomMove(event, true);
                return;
            }

            GameMove gameMove = bestMoveStrategy.findBestMove(event.getBoard(), getBestDepth(event.getBoard().size()));

            try {
                event.makeMove(this, gameMove);
            } catch(InvalidMoveException e) {
                // algorithm failed, need to report that and make a random move
                // to keep the game going.
                e.printStackTrace();
                makeRandomMove(event, false);
            }
        }
    }

    @Override
    public void handleEvent(GameEventType eventType, GameEvent event) {
        switch (eventType) {
            case GAME_READY:
            case PLAYER_MOVED:
                this.handleGameStillOnEvents(event);
                break;
        }
    }

}
