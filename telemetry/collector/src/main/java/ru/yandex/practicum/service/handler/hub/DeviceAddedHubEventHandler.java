package ru.yandex.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.hub.DeviceAddedEvent;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.hub.HubEventType;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.util.Utils;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@Component
public class DeviceAddedHubEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {

    public DeviceAddedHubEventHandler(KafkaEventProducer eventProducer) {
        super(eventProducer);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    public HubEventProto.PayloadCase getMessageTypeGrpc() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEvent event) {
        DeviceAddedEvent specificEvent = (DeviceAddedEvent) event;
        return DeviceAddedEventAvro.newBuilder()
                .setId(specificEvent.getId())
                .setType(Utils.mapEnum(specificEvent.getDeviceType(), DeviceTypeAvro.class))
                .build();
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEventProto event) {
        DeviceAddedEventProto specificEvent = event.getDeviceAdded();
        return DeviceAddedEventAvro.newBuilder()
                .setId(specificEvent.getId())
                .setType(Utils.mapEnum(specificEvent.getType(), DeviceTypeAvro.class))
                .build();
    }
}
