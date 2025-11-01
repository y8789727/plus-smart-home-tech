package ru.practicum.service.handler.hub;

import ru.practicum.dto.hub.HubEventType;
import ru.practicum.dto.hub.HubEvent;

public interface HubEventHandler {

    HubEventType getMessageType();

    void handle(HubEvent event);
}
