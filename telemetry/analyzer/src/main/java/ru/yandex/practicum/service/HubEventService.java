package ru.yandex.practicum.service;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventService {

    void addHubDevice(HubEventAvro event);

    void removeHubDevice(HubEventAvro event);

    void addScenario(HubEventAvro event);

    void removeScenario(HubEventAvro event);

}
