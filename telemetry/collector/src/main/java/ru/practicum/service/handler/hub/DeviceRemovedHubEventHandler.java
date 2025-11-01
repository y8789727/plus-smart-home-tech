package ru.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.dto.hub.DeviceRemovedEvent;
import ru.practicum.dto.hub.HubEvent;
import ru.practicum.dto.hub.HubEventType;
import ru.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
public class DeviceRemovedHubEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {

    public DeviceRemovedHubEventHandler(KafkaEventProducer eventProducer) {
        super(eventProducer);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEvent event) {
        DeviceRemovedEvent specificEvent = (DeviceRemovedEvent) event;
        return DeviceRemovedEventAvro.newBuilder()
                .setId(specificEvent.getId())
                .build();
    }
}
