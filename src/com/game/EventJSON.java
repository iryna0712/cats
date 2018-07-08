package com.game;

import com.entities.CardType;

import java.io.Serializable;

public class EventJSON implements Serializable {
    public enum EventJSONType {
        START,
        TURN,
        TAKE,
        RECEIVE,
        PLAY,
        TAKE_FROM_DECK,
        AMOUNT_CHANGED,
        SHOW_CARDS,
        PLACE_BOMB
    }

    private EventJSONType event;

    public EventJSON(EventJSONType event) {
        this.event = event;
    }
}
