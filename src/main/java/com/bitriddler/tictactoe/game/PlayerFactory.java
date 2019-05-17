package com.bitriddler.tictactoe.game;

import com.bitriddler.tictactoe.game.ai.AiPlayer;
import com.bitriddler.tictactoe.game.ai.MinMaxBestMoveStrategy;
import com.bitriddler.tictactoe.game.boardDisplay.BoardBasicViewStrategy;
import com.bitriddler.tictactoe.game.boardDisplay.BoardViewStrategy;
import com.bitriddler.tictactoe.game.moveParser.CommaMoveParserStrategy;
import com.bitriddler.tictactoe.game.moveParser.MoveParserStrategy;

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
        MinMaxBestMoveStrategy bestMoveStrategy = new MinMaxBestMoveStrategy(hPlayers);
        AiPlayer aiPlayer = new AiPlayer(
                symbol,
                bestMoveStrategy
        );
        bestMoveStrategy.setAiPlayer(aiPlayer);
        return aiPlayer;
    }
}
