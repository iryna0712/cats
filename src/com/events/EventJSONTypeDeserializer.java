package com.events;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class EventJSONTypeDeserializer extends StdDeserializer<EventJSON.EventJSONType> {

    public EventJSONTypeDeserializer() {
        this(null);
    }

    public EventJSONTypeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public EventJSON.EventJSONType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.textValue();

        return EventJSON.EventJSONType.fromJSONString(value);
    }
}
