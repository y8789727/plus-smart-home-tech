package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.sensor.MotionSensorEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.dto.sensor.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.producer.KafkaEventProducer;
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
    public SensorEventProto.PayloadCase getMessageTypeGrpc() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
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

    @Override
    protected MotionSensorAvro mapToAvro(SensorEventProto event) {
        MotionSensorProto specificEvent = event.getMotionSensor();
        return MotionSensorAvro.newBuilder()
                .setMotion(specificEvent.getMotion())
                .setVoltage(specificEvent.getVoltage())
                .setLinkQuality(specificEvent.getLinkQuality())
                .build();
    }
}
