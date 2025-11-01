package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.dto.sensor.SensorEventType;
import ru.yandex.practicum.dto.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {

    public TemperatureSensorEventHandler(KafkaEventProducer eventProducer) {
        super(eventProducer);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEvent event) {
        TemperatureSensorEvent specificEvent = (TemperatureSensorEvent) event;
        return TemperatureSensorAvro.newBuilder()
                .setId(specificEvent.getId())
                .setHubId(specificEvent.getHubId())
                .setTimestamp(specificEvent.getTimestamp())
                .setTemperatureC(specificEvent.getTemperatureC())
                .setTemperatureF(specificEvent.getTemperatureF())
                .build();
    }
}
