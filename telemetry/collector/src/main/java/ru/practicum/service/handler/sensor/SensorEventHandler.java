package ru.practicum.service.handler.sensor;

import ru.practicum.dto.sensor.SensorEvent;
import ru.practicum.dto.sensor.SensorEventType;

public interface SensorEventHandler {

    SensorEventType getMessageType();

    void handle(SensorEvent event);
}
