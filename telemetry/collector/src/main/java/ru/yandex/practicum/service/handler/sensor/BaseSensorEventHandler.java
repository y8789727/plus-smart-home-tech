package ru.yandex.practicum.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.producer.KafkaConfig;
import ru.yandex.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.util.Utils;

public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {
    protected final KafkaEventProducer producer;

    protected BaseSensorEventHandler(KafkaEventProducer producer) {
        this.producer = producer;
    }

    protected abstract T mapToAvro(SensorEvent event);

    protected abstract T mapToAvro(SensorEventProto event);

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

    @Override
    public void handle(SensorEventProto event) {
        if (!event.getPayloadCase().equals(this.getMessageTypeGrpc())) {
            throw new IllegalArgumentException("Неподдерживаемый тип события gRPC " + event.getPayloadCase());
        }

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Utils.timestampToInstant(event.getTimestamp()))
                .setPayload(this.mapToAvro(event))
                .build();

        producer.send(eventAvro, event.getHubId(), Utils.timestampToInstant(event.getTimestamp()), KafkaConfig.TopicType.SENSOR_EVENTS);
    }
}
