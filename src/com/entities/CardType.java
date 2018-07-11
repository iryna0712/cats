package com.entities;

import com.events.serializers.CardTypeDeserializer;
import com.events.JSONConvertible;
import com.events.serializers.JSONConvertibleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = JSONConvertibleSerializer.class)
@JsonDeserialize(using = CardTypeDeserializer.class)
public enum CardType implements JSONConvertible {
    BOMB("bomb"),
    SHIELD("defuse"),
    FUTURE("predict"),
    PLEASE("please"),
    ATTACK("attack"),
    SKIP("skip"),
    SHUFFLE("shuffle"),
    STOP("stop"),
    //below only paired cards
    SHAWERMA("shawerma"),
    WATERMELON("watermelon"),
    CUCUMBER("cucumber"),
    RAINBOW("rainbow"),
    LUMBERJACK("lumberjack");

    CardType(String jsonString) {
        this.jsonString = jsonString;
    }

    private String jsonString;

    public static boolean isPairCard(CardType type) {
        return type.equals(SHAWERMA) || type.equals(WATERMELON) ||
                type.equals(CUCUMBER) || type.equals(RAINBOW) ||
                type.equals(LUMBERJACK);
    }

    public static boolean isCardStoppable(CardType type) {
        //TODO: this is TEMPORARY!!!!
        return type != BOMB && type != SHIELD && type != SKIP && type != CUCUMBER;
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
