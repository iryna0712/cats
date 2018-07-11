package com.events.serializers;

import com.events.JSONConvertible;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class JSONConvertibleDeserializer extends StdDeserializer<JSONConvertible> {

    public JSONConvertibleDeserializer() {
        this(null);
    }

    public JSONConvertibleDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public JSONConvertible deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.textValue();

        //Class<?> classType = this.getValueClass();
        JavaType type = this.getValueType();
        Class<?> handledType = this.handledType();

//        //int id = (Integer) ((IntNode) node.get("id")).numberValue();
//        //String itemName = node.get("itemName").asText();
//        //int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();


//
//        return new Item(id, itemName, new User(userId, null));
        return (JSONConvertible)new Object();
    }
}
