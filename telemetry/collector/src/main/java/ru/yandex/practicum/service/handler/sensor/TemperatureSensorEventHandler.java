package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.dto.sensor.SensorEventType;
import ru.yandex.practicum.dto.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.util.Utils;

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
    public SensorEventProto.PayloadCase getMessageTypeGrpc() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
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

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEventProto event) {
        TemperatureSensorProto specificEvent = event.getTemperatureSensor();
        return TemperatureSensorAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Utils.timestampToInstant(event.getTimestamp()))
                .setTemperatureC(specificEvent.getTemperatureC())
                .setTemperatureF(specificEvent.getTemperatureF())
                .build();
    }
}
