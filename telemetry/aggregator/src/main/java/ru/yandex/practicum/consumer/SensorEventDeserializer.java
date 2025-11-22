package ru.yandex.practicum.consumer;

import ru.yandex.practicum.kafka.deserializer.BaseAvroDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public class SensorEventDeserializer extends BaseAvroDeserializer<SensorEventAvro> {
    public SensorEventDeserializer() {
        super(SensorEventAvro.getClassSchema());
    }
}
