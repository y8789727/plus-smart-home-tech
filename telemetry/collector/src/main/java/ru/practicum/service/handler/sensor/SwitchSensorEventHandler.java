package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.dto.sensor.SensorEvent;
import ru.practicum.dto.sensor.SensorEventType;
import ru.practicum.dto.sensor.SwitchSensorEvent;
import ru.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {

    public SwitchSensorEventHandler(KafkaEventProducer eventProducer) {
        super(eventProducer);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEvent event) {
        SwitchSensorEvent specificEvent = (SwitchSensorEvent) event;
        return SwitchSensorAvro.newBuilder()
                .setState(specificEvent.getState())
                .build();
    }
}
