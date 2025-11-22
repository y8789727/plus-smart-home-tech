package ru.yandex.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.hub.DeviceRemovedEvent;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.hub.HubEventType;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.producer.KafkaEventProducer;
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
    public HubEventProto.PayloadCase getMessageTypeGrpc() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEvent event) {
        DeviceRemovedEvent specificEvent = (DeviceRemovedEvent) event;
        return DeviceRemovedEventAvro.newBuilder()
                .setId(specificEvent.getId())
                .build();
    }

    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEventProto event) {
        DeviceRemovedEventProto specificEvent = event.getDeviceRemoved();
        return DeviceRemovedEventAvro.newBuilder()
                .setId(specificEvent.getId())
                .build();
    }
}
