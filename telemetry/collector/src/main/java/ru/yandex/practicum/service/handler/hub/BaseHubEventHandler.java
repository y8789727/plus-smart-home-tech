package ru.yandex.practicum.service.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.producer.KafkaConfig;
import ru.yandex.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.util.Utils;


public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements  HubEventHandler {
    protected final KafkaEventProducer producer;

    protected BaseHubEventHandler(KafkaEventProducer producer) {
        this.producer = producer;
    }

    protected abstract T mapToAvro(HubEvent event);

    protected abstract T mapToAvro(HubEventProto event);

    @Override
    public void handle(HubEvent event) {
        if (!event.getType().equals(this.getMessageType())) {
            throw new IllegalArgumentException("Неподдерживаемый тип события " + event.getType());
        }

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(this.mapToAvro(event))
                .build();

        producer.send(eventAvro, event.getHubId(), event.getTimestamp(), KafkaConfig.TopicType.HUB_EVENTS);
    }

    @Override
    public void handle(HubEventProto event) {
        if (!event.getPayloadCase().equals(this.getMessageTypeGrpc())) {
            throw new IllegalArgumentException("Неподдерживаемый тип события gRPC " + event.getPayloadCase());
        }

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Utils.timestampToInstant(event.getTimestamp()))
                .setPayload(this.mapToAvro(event))
                .build();

        producer.send(eventAvro, event.getHubId(), Utils.timestampToInstant(event.getTimestamp()), KafkaConfig.TopicType.HUB_EVENTS);
    }
}
