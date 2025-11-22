package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.hub.HubEventType;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.dto.sensor.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.service.handler.hub.HubEventHandler;
import ru.yandex.practicum.service.handler.sensor.SensorEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventsServiceImpl implements EventsService {
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubEventHandlersGrpc;
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlersGrpc;

    public EventsServiceImpl(Set<HubEventHandler> hubEventHandlers, Set<SensorEventHandler> sensorEventHandlers) {
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventHandlersGrpc = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageTypeGrpc, Function.identity()));
        this.sensorEventHandlersGrpc = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageTypeGrpc, Function.identity()));
    }

    @Override
    public void collectServiceEvent(SensorEvent event) {
        SensorEventHandler handler = sensorEventHandlers.get(event.getType());
        if (handler == null) {
            throw new IllegalArgumentException("Не найден обработчик для события типа " + event.getType());
        }
        handler.handle(event);
    }

    @Override
    public void collectServiceEvent(SensorEventProto event) {
        SensorEventHandler handler = sensorEventHandlersGrpc.get(event.getPayloadCase());
        if (handler == null) {
            throw new IllegalArgumentException("Не найден обработчик для события типа " + event.getPayloadCase());
        }
        handler.handle(event);
    }

    @Override
    public void collectHubEvent(HubEvent event) {
        HubEventHandler handler = hubEventHandlers.get(event.getType());
        if (handler == null) {
            throw new IllegalArgumentException("Не найден обработчик для события типа " + event.getType());
        }
        handler.handle(event);
    }

    @Override
    public void collectHubEvent(HubEventProto event) {
        HubEventHandler handler = hubEventHandlersGrpc.get(event.getPayloadCase());
        if (handler == null) {
            throw new IllegalArgumentException("Не найден обработчик для события типа " + event.getPayloadCase());
        }
        handler.handle(event);
    }
}
