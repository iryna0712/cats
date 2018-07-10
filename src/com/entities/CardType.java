package com.entities;

import com.events.CardTypeDeserializer;
import com.events.JSONConvertible;
import com.events.JSONConvertibleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = JSONConvertibleSerializer.class)
@JsonDeserialize(using = CardTypeDeserializer.class)
public enum CardType implements JSONConvertible {
    BOMB("bomb"),
    SHIELD("defuse"),
    PREDICTION("predict"),
    RECEIVE_PLAYERS_CARD("please"),
    ATTACK("attack"),
    SKIP_MOVE("skip"),
    SHUFFLE("shuffle"),
    STOP("stop"),
    //below only paired cards
    SHAWERMA("shawerma"),
    WATERMELON("watermelon"),
    CUCUMBER("cucumber"),
    RAINBOW_CAT("rainbow"),
    LUMBERJACK("lumberjack");

    CardType(String jsonString) {
        this.jsonString = jsonString;
    }

    private String jsonString;

    public static boolean isPairCard(CardType type) {
        return type.equals(SHAWERMA) || type.equals(WATERMELON) ||
                type.equals(CUCUMBER) || type.equals(RAINBOW_CAT) ||
                type.equals(LUMBERJACK);
    }

    public static boolean isCardStoppable(CardType type) {
        return type != BOMB && type != SHIELD;
    }

    public String toJSONString() {
        return jsonString;
    }

    public static CardType fromJSONString(String jsonString) {
        CardType result = null;
        CardType[] values = CardType.values();
        for (CardType value : values) {
            if (value.jsonString.equals(jsonString)) {
                result = value;
            }
        }
        return result;
    }
}
