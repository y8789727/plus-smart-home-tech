package ru.yandex.practicum.dto.hub;

import lombok.Getter;

@Getter
public class DeviceAction {
    private String sensorId;
    private DeviceActionType type;
    private Integer value;
}
