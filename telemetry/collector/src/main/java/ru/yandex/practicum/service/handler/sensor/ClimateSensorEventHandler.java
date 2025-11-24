package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.sensor.ClimateSensorEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.dto.sensor.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.producer.KafkaEventProducer;
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
    public SensorEventProto.PayloadCase getMessageTypeGrpc() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
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

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEventProto event) {
        ClimateSensorProto specificEvent = event.getClimateSensor();
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(specificEvent.getCo2Level())
                .setHumidity(specificEvent.getHumidity())
                .setTemperatureC(specificEvent.getTemperatureC())
                .build();
    }
}
