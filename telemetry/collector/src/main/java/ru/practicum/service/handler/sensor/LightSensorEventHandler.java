package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.dto.sensor.LightSensorEvent;
import ru.practicum.dto.sensor.SensorEvent;
import ru.practicum.dto.sensor.SensorEventType;
import ru.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {

    public LightSensorEventHandler(KafkaEventProducer eventProducer) {
        super(eventProducer);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEvent event) {
        LightSensorEvent specificEvent = (LightSensorEvent) event;
        return LightSensorAvro.newBuilder()
                .setLinkQuality(specificEvent.getLinkQuality())
                .setLuminosity(specificEvent.getLuminosity())
                        .build();
    }
}
