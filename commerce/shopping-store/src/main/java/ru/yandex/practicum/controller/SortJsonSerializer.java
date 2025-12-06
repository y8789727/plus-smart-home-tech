package ru.yandex.practicum.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Sort;

import java.io.IOException;

@JsonComponent
public class SortJsonSerializer extends JsonSerializer<Sort> {
    @Override
    public void serialize(Sort orders, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();

        orders.iterator().forEachRemaining(v -> {
            try {
                jsonGenerator.writeObject(v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        jsonGenerator.writeEndArray();
    }

    @Override
    public Class<Sort> handledType() {
        return Sort.class;
    }
}

