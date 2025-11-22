package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.model.Condition;
import ru.yandex.practicum.dal.model.ConditionType;
import ru.yandex.practicum.dal.model.Scenario;
import ru.yandex.practicum.dal.repository.ScenarioRepository;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import com.google.protobuf.Timestamp;
import ru.yandex.practicum.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotServiceImpl implements SnapshotService {

    private final ScenarioRepository scenarioRepository;
    @GrpcClient("hub-router")
    private HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    private final Map<ConditionType, Map<Class<?>, Function<SensorStateAvro, Integer>>> sensorValueGetters = defineSensorValueGetters();

    @Override
    public void processSnapshot(SensorsSnapshotAvro snapshot) {

        final Timestamp snapshotTimestamp = Timestamp.newBuilder()
                .setSeconds(snapshot.getTimestamp().getEpochSecond())
                .setNanos(snapshot.getTimestamp().getNano())
                .build();

        scenarioRepository.findByHubId(snapshot.getHubId()).stream()
                .filter(s -> this.allConditionsTrue(snapshot, s.getConditions()))
                .forEach(s -> this.doActions(s, snapshotTimestamp));
    }

    private boolean allConditionsTrue(SensorsSnapshotAvro snapshot, Map<String, Condition> conditions) {
        log.info("Analyzer-SnapshotServiceImpl-allConditionsTrue: \n snapshot={} \n conditions={}", snapshot, conditions);
        return conditions.entrySet().stream()
                .allMatch(sensorCondition -> calcCondition(sensorCondition.getValue(), snapshot.getSensorsState().get(sensorCondition.getKey())));
    }

    private boolean calcCondition(Condition condition, SensorStateAvro sensorState) {
        log.info("Analyzer-SnapshotServiceImpl-calcCondition: \n condition={} \n sensorState={}", condition, sensorState);
        if (sensorState == null) {
            log.info("Analyzer-SnapshotServiceImpl-calcCondition: sensorState is null");
            return false;
        }

        return sensorValueGetters
                .get(condition.getType())
                .get(sensorState.getData().getClass())
                .apply(sensorState).compareTo(condition.getValue()) ==
                switch (condition.getOperation()) {
                    case LOWER_THAN -> -1;
                    case EQUALS -> 0;
                    case GREATER_THAN -> 1;
        };
    }

    private void doActions(Scenario scenario, Timestamp timestamp) {
        log.info("Analyzer-SnapshotServiceImpl-doActions: actions={}", scenario.getActions());
        scenario.getActions().entrySet().stream().map(e ->
                DeviceActionRequest.newBuilder()
                        .setHubId(scenario.getHubId())
                        .setScenarioName(scenario.getName())
                        .setTimestamp(timestamp)
                        .setAction(DeviceActionProto.newBuilder()
                                .setSensorId(e.getKey())
                                .setType(Utils.mapEnum(e.getValue().getType(), ActionTypeProto.class))
                                .setValue(e.getValue().getValue() != null ? e.getValue().getValue() : 0)
                                .build())
                        .build()
        ).forEach(hubRouterClient::handleDeviceAction);
    }

    private static Map<ConditionType, Map<Class<?>, Function<SensorStateAvro, Integer>>> defineSensorValueGetters() {
        Map<ConditionType, Map<Class<?>, Function<SensorStateAvro, Integer>>> valueGetters = new HashMap<>();

        valueGetters.put(ConditionType.MOTION, Map.of(
                MotionSensorAvro.class, s -> ((MotionSensorAvro) s.getData()).getMotion() ? 1 : 0
        ));

        valueGetters.put(ConditionType.LUMINOSITY, Map.of(
                LightSensorAvro.class, s -> ((LightSensorAvro) s.getData()).getLuminosity()
        ));

        valueGetters.put(ConditionType.SWITCH, Map.of(
                SwitchSensorAvro.class, s -> ((SwitchSensorAvro) s.getData()).getState() ? 1 : 0
        ));

        valueGetters.put(ConditionType.TEMPERATURE, Map.of(
                ClimateSensorAvro.class, s -> ((ClimateSensorAvro) s.getData()).getTemperatureC(),
                TemperatureSensorAvro.class, s -> ((TemperatureSensorAvro) s.getData()).getTemperatureC()
        ));

        valueGetters.put(ConditionType.CO2LEVEL, Map.of(
                ClimateSensorAvro.class, s -> ((ClimateSensorAvro) s.getData()).getCo2Level()
        ));

        valueGetters.put(ConditionType.HUMIDITY, Map.of(
                ClimateSensorAvro.class, s -> ((ClimateSensorAvro) s.getData()).getHumidity()
        ));

        return valueGetters;
    }
}
