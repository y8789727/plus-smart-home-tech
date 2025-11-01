package ru.practicum.service;

import ru.practicum.dto.hub.HubEvent;
import ru.practicum.dto.sensor.SensorEvent;

public interface EventsService {

    void collectServiceEvent(SensorEvent event);

    void collectHubEvent(HubEvent event);
}
