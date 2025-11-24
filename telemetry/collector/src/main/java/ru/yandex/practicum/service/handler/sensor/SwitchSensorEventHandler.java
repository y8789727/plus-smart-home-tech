package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.dto.sensor.SensorEventType;
import ru.yandex.practicum.dto.sensor.SwitchSensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.producer.KafkaEventProducer;
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
    public SensorEventProto.PayloadCase getMessageTypeGrpc() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEvent event) {
        SwitchSensorEvent specificEvent = (SwitchSensorEvent) event;
        return SwitchSensorAvro.newBuilder()
                .setState(specificEvent.getState())
                .build();
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEventProto event) {
        SwitchSensorProto specificEvent = event.getSwitchSensor();
        return SwitchSensorAvro.newBuilder()
                .setState(specificEvent.getState())
                .build();
    }
}
