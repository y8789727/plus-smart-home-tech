package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;

public interface EventsService {

    void collectServiceEvent(SensorEvent event);

    void collectHubEvent(HubEvent event);
}
