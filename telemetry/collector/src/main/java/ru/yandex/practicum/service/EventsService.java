package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface EventsService {

    void collectServiceEvent(SensorEvent event);

    void collectServiceEvent(SensorEventProto event);

    void collectHubEvent(HubEvent event);

    void collectHubEvent(HubEventProto event);
}
