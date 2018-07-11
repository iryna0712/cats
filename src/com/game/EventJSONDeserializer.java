package com.game;

import com.events.custom.ConnectEventJSON;
import com.events.EventJSON;
import com.events.custom.PlaceBombEventJSON;
import com.events.custom.PlayCardEventJSON;
import com.events.custom.TakeFromDeckEventJSON;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class EventJSONDeserializer  extends StdDeserializer<EventJSON> {

        public EventJSONDeserializer() {
            this(null);
        }

        public EventJSONDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public EventJSON deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException {

            JsonNode node = jp.getCodec().readTree(jp);

            String originalJSON = node.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            EventJSON eventJSON = null;

            String event = node.get("event").asText();

            try {
                switch (event) {
                    case "connect":
                        eventJSON = objectMapper.readValue(originalJSON, ConnectEventJSON.class);
                        break;
                    case "play":
                        eventJSON = objectMapper.readValue(originalJSON, PlayCardEventJSON.class);
                        break;
                    case "takeFromDeck":
                        eventJSON = objectMapper.readValue(originalJSON, TakeFromDeckEventJSON.class);
                        break;
                    case "placeBomb":
                        eventJSON = objectMapper.readValue(originalJSON, PlaceBombEventJSON.class);
                        break;
                    default:
                        break;

                }
            } catch (Exception e) {
                int a = 5;
            }
            return eventJSON;
        }
}
