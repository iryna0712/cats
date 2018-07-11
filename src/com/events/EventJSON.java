package com.events;

import com.events.serializers.EventJSONTypeDeserializer;
import com.events.serializers.JSONConvertibleSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

public class EventJSON implements Serializable {

    @JsonSerialize(using = JSONConvertibleSerializer.class)
    @JsonDeserialize(using = EventJSONTypeDeserializer.class)
    public enum EventJSONType implements JSONConvertible {
        START("start"),
        HANDSHAKE("connect"),
        TURN("turn"),
        TAKE("take"),
        RECEIVE("receive"),
        PLAY("play"),
        TAKE_FROM_DECK("takeFromDeck"),
        AMOUNT_CHANGED("amountChanged"),
        SHOW_CARDS("showCard"),
        PLACE_BOMB("placeBomb"),
        DISCONNECT("disconnected"),
        COOLDOWN_FINISHED("noStopCard"),
        GIVE_ANY_CARD("giveAnyCard"),
        GAME_OVER("gameOver");

        private String jsonString;

        EventJSONType() {}
        EventJSONType(String jsonString) {
            this.jsonString = jsonString;
        }

        public void setJsonString(String jsonString) {
            this.jsonString = jsonString;
        }

        @Override
        public String toJSONString() {
            return jsonString;
        }

        public static EventJSONType fromJSONString(String jsonString) {
            EventJSONType result = null;
            EventJSONType[] values = EventJSONType.values();
            for (EventJSONType value : values) {
                if (value.jsonString.equals(jsonString)) {
                    result = value;
                }
            }
            return result;
        }
    }

    @JsonProperty(value="event")
    private EventJSONType type;

    //default constructor for deserialization
    public EventJSON() {}
    public EventJSON(EventJSONType event) {
        this.type = event;
    }

    public EventJSONType getType() { return type; }
    public void setType(EventJSONType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "EventJSON{" +
                "event=" + type +
                '}';
    }
}
