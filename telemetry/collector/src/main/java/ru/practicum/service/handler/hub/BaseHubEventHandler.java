package ru.practicum.service.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.dto.hub.HubEvent;
import ru.practicum.producer.KafkaConfig;
import ru.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;


public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements  HubEventHandler {
    protected final KafkaEventProducer producer;

    protected BaseHubEventHandler(KafkaEventProducer producer) {
        this.producer = producer;
    }

    protected abstract T mapToAvro(HubEvent event);

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
}
