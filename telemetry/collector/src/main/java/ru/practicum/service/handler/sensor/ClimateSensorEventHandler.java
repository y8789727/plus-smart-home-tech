package ru.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.dto.sensor.ClimateSensorEvent;
import ru.practicum.dto.sensor.SensorEvent;
import ru.practicum.dto.sensor.SensorEventType;
import ru.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {

    public ClimateSensorEventHandler(KafkaEventProducer eventProducer) {
        super(eventProducer);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEvent event) {
        ClimateSensorEvent specificEvent = (ClimateSensorEvent) event;
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(specificEvent.getCo2Level())
                .setHumidity(specificEvent.getHumidity())
                .setTemperatureC(specificEvent.getTemperatureC())
                .build();
    }
}
