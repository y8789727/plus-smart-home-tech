package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.dto.sensor.MotionSensorEvent;
import ru.practicum.dto.sensor.SensorEvent;
import ru.practicum.dto.sensor.SensorEventType;
import ru.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {

    public MotionSensorEventHandler(KafkaEventProducer eventProducer) {
        super(eventProducer);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    protected MotionSensorAvro mapToAvro(SensorEvent event) {
        MotionSensorEvent specificEvent = (MotionSensorEvent) event;
        return MotionSensorAvro.newBuilder()
                .setMotion(specificEvent.getMotion())
                .setVoltage(specificEvent.getVoltage())
                .setLinkQuality(specificEvent.getLinkQuality())
                .build();
    }
}
