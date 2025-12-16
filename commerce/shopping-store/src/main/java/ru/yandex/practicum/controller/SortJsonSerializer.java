package ru.yandex.practicum.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Sort;

import java.io.IOException;

@Slf4j
@JsonComponent
public class SortJsonSerializer extends JsonSerializer<Sort> {
    @Override
    public void serialize(Sort orders, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();

        orders.iterator().forEachRemaining(v -> {
            try {
                jsonGenerator.writeObject(v);
            } catch (IOException e) {
                log.error("Ошибка при сериализация Sort: {}", e.getMessage());
            }
        });

        jsonGenerator.writeEndArray();
    }

    @Override
    public Class<Sort> handledType() {
        return Sort.class;
    }
}

