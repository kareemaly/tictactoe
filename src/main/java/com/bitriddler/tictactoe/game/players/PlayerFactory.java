package com.bitriddler.tictactoe.game.players;

import com.bitriddler.tictactoe.game.GameBoard;
import com.bitriddler.tictactoe.game.GameIO;
import com.bitriddler.tictactoe.game.ai.MinMaxBestMoveStrategy;
import com.bitriddler.tictactoe.game.boardDisplay.BoardBasicViewStrategy;
import com.bitriddler.tictactoe.game.boardDisplay.BoardViewStrategy;
import com.bitriddler.tictactoe.game.moveParser.CommaMoveParserStrategy;
import com.bitriddler.tictactoe.game.moveParser.MoveParserStrategy;
import com.bitriddler.tictactoe.game.winner.StandardWinnerStrategy;
import com.bitriddler.tictactoe.game.winner.WinnerStrategy;

public class PlayerFactory {
    public Player makeHumanPlayer(GameBoard board, char symbol, GameIO gameIO) {
        BoardViewStrategy viewStrategy = new BoardBasicViewStrategy(board);
        MoveParserStrategy moveParserStrategy = new CommaMoveParserStrategy();

        return new HumanPlayer(
                symbol,
                gameIO,
                viewStrategy,
                moveParserStrategy
        );
    }
    public Player makeAiPlayer(char symbol, Player[] hPlayers) {
        WinnerStrategy winnerStrategy = new StandardWinnerStrategy();
        MinMaxBestMoveStrategy bestMoveStrategy = new MinMaxBestMoveStrategy(winnerStrategy, hPlayers);
        AiPlayer aiPlayer = new AiPlayer(
                symbol,
                bestMoveStrategy
        );
        bestMoveStrategy.setAiPlayer(aiPlayer);
        return aiPlayer;
    }
}
