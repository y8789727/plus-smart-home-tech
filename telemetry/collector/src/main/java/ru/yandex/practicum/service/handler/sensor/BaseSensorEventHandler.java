package ru.yandex.practicum.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.producer.KafkaConfig;
import ru.yandex.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {
    protected final KafkaEventProducer producer;

    protected BaseSensorEventHandler(KafkaEventProducer producer) {
        this.producer = producer;
    }

    protected abstract T mapToAvro(SensorEvent event);

    @Override
    public void handle(SensorEvent event) {
        if (!event.getType().equals(this.getMessageType())) {
            throw new IllegalArgumentException("Неподдерживаемый тип события " + event.getType());
        }

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(this.mapToAvro(event))
                .build();

        producer.send(eventAvro, event.getHubId(), event.getTimestamp(), KafkaConfig.TopicType.SENSOR_EVENTS);
    }
}
