package com.navimee.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer extends StdSerializer<Date> {
    private SimpleDateFormat formatter
            = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    public DateSerializer() {
        this(null);
    }

    public DateSerializer(Class t) {
        super(t);
    }

    @Override
    public void serialize (Date value, JsonGenerator gen, SerializerProvider arg2) throws IOException {
        String date = formatter.format(value);
        gen.writeString(date);
    }
}
