package com.bitriddler.tictactoe.game.events;

public interface GameEventSubscriber {
    void handleEvent(GameEventType eventType, GameEvent event);
}
