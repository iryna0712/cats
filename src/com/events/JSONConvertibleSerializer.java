package com.events;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class JSONConvertibleSerializer extends StdSerializer<JSONConvertible> {

    public JSONConvertibleSerializer() {
        this(null);
    }

    public JSONConvertibleSerializer(Class<JSONConvertible> t) {
            super(t);
    }

    @Override
    public void serialize(JSONConvertible value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            //jgen.writeStartObject();
            //jgen.writeStringField("field", value.toJSONString());
            jgen.writeString(value.toJSONString());
            //jgen.writeEndObject();
        }
}
