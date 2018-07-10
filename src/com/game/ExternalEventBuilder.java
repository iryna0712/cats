package com.game;

import com.events.EventJSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

public class ExternalEventBuilder {

    public static EventJSON parseEvent(String message) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(EventJSON.class, new EventJSONDeserializer());
        mapper.registerModule(module);

        EventJSON readValue = null;
        try {
            readValue = mapper.readValue(message, EventJSON.class);
        } catch (IOException e) {
            System.out.println("error parsing message: " + message);
        }

        return readValue;
    }

    public static EventJSON buildEvent() {
        return null;
    }
}
