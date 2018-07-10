package com.events;

import com.entities.CardType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CardTypeDeserializer  extends StdDeserializer<CardType> {

    public CardTypeDeserializer() {
        this(null);
    }

    public CardTypeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public CardType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.textValue();

        return CardType.fromJSONString(value);
    }
}
