package com.bitriddler.tictactoe.game;

import com.bitriddler.tictactoe.game.events.GameEvent;
import com.bitriddler.tictactoe.game.events.GameEventSubscriber;
import com.bitriddler.tictactoe.game.events.GameEventType;

import java.util.ArrayList;
import java.util.HashMap;


public class GameEventPublisher {
    private HashMap<GameEventType, ArrayList<GameEventSubscriber>> listeners = new HashMap<>();

    public void publishEvent(GameEventType eventType, GameEvent event) {
        ArrayList<GameEventSubscriber> subscribers = listeners.get(eventType);

        if (subscribers == null) {
            return;
        }

        for (GameEventSubscriber subscriber: subscribers) {
            new Thread(() -> subscriber.handleEvent(eventType, event)).start();
        }
    }

    public void addSubscriberFor(GameEventType eventType, GameEventSubscriber subscriber) {
        if (listeners.get(eventType) != null) {
            listeners.get(eventType).add(subscriber);
        } else {
            ArrayList<GameEventSubscriber> subscribers = new ArrayList<>();
            subscribers.add(subscriber);
            listeners.put(eventType, subscribers);
        }
    }

    public void addSubscriberForAllEvents(GameEventSubscriber subscriber) {
        for(GameEventType eventType : GameEventType.values()) {
            this.addSubscriberFor(eventType, subscriber);
        }
    }
}
