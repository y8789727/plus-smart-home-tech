package ru.yandex.practicum.service.handler.hub;

import ru.yandex.practicum.dto.hub.HubEventType;
import ru.yandex.practicum.dto.hub.HubEvent;

public interface HubEventHandler {

    HubEventType getMessageType();

    void handle(HubEvent event);
}
