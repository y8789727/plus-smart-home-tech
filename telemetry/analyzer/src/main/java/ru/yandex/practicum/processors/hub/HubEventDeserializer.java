package ru.yandex.practicum.processors.hub;

import ru.yandex.practicum.kafka.deserializer.BaseAvroDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public class HubEventDeserializer extends BaseAvroDeserializer<HubEventAvro> {
    public HubEventDeserializer() {
        super(HubEventAvro.getClassSchema());
    }
}