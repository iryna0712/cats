package com.events.serializers;

import com.entities.Card;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CardSerializer extends StdSerializer<Card> {

    public CardSerializer() {
        this(null);
    }

    public CardSerializer(Class<Card> t) {
        super(t);
    }

    @Override
    public void serialize(Card card, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String serializedCardType = mapper.writeValueAsString(card.getType());
        //TODO why double quoutes here?
        serializedCardType = serializedCardType.replace("\"", "");
        jsonGenerator.writeString(serializedCardType);
    }
}
