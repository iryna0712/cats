package com.events.serializers;

import com.entities.Deck;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class PlaceJSONDeserializer extends StdDeserializer<Deck.Place> {

    public PlaceJSONDeserializer() {
        this(null);
    }

    public PlaceJSONDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Deck.Place deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.textValue();

        return Deck.Place.fromJSONString(value);
    }
}

