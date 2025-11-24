package ru.yandex.practicum.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

@Slf4j
@Component
public class SensorSnapshotStorageInMemory implements SensorSnapshotStorage {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();
    private final Map<Class<?>, BiPredicate<Object, Object>> comparators = buildComparatorsMap();

    @Override
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        String hubId = event.getHubId();

        SensorsSnapshotAvro snapshot = snapshots.get(hubId);
        if (snapshot == null) {
            log.info("Aggregator: Snapshot not found, creating...");
            snapshot = SensorsSnapshotAvro.newBuilder()
                        .setHubId(hubId)
                        .setTimestamp(event.getTimestamp())
                        .setSensorsState(new HashMap<>())
                        .build();
            snapshots.put(hubId, snapshot);
        }
        log.info("Aggregator: Snapshot hubId={}", snapshot.getHubId());

        log.info("Aggregator: Searching for sensor by id={}", event.getId());
        SensorStateAvro sensorState = snapshot.getSensorsState().get(event.getId());
        if (sensorState != null) {
            log.info("Aggregator: sensor state found, comparing and updating...");
            if (sensorState.getTimestamp().isAfter(event.getTimestamp())) {
                log.info("Aggregator: existing timestamp is later than incoming, returning Empty!");
                return Optional.empty();
            } else if (!updateSensorState(sensorState, event)) {
                log.info("Aggregator: no need to update sensor state, returning Empty");
                return Optional.empty();
            }
        } else {
            log.info("Aggregator: sensor state NOT found creating...");
            sensorState = SensorStateAvro.newBuilder()
                    .setTimestamp(event.getTimestamp())
                    .setData(event.getPayload())
                    .build();
            log.info("Aggregator: adding sensorState for id={}", event.getId());
            snapshot.getSensorsState().put(event.getId(), sensorState);
        }
        snapshot.setTimestamp(sensorState.getTimestamp().isAfter(snapshot.getTimestamp()) ? sensorState.getTimestamp() : snapshot.getTimestamp());

        log.info("Aggregator: returning snapshot={}", snapshot);

        return Optional.of(snapshot);
    }

    private boolean updateSensorState(SensorStateAvro state, SensorEventAvro event) {

        BiPredicate<Object, Object> predicate = comparators.get(state.getData().getClass());

        if (predicate == null) {
            throw new IllegalArgumentException("Не найден компаратор для датчиков класса " + state.getData().getClass().getSimpleName());
        }

        try {
            if (!predicate.test(state.getData(), event.getPayload())) {
                state.setTimestamp(event.getTimestamp());
                state.setData(event.getPayload());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка сравнения показания датчиков");
        }
    }

    private static Map<Class<?>, BiPredicate<Object, Object>> buildComparatorsMap() {
        Map<Class<?>, BiPredicate<Object, Object>> comparators = new HashMap<>();

        comparators.put(SwitchSensorAvro.class,
                (a,  b) -> {
                    SwitchSensorAvro first = (SwitchSensorAvro) a;
                    SwitchSensorAvro second = (SwitchSensorAvro) b;
                    return first.getState() == second.getState();
                });

        comparators.put(MotionSensorAvro.class,
                (a,  b) -> {
                    MotionSensorAvro first = (MotionSensorAvro) a;
                    MotionSensorAvro second = (MotionSensorAvro) b;
                    return first.getMotion() == second.getMotion()
                           && first.getVoltage() == second.getVoltage()
                           && first.getLinkQuality() == second.getLinkQuality();
                });

        comparators.put(LightSensorAvro.class,
                (a,  b) -> {
                    LightSensorAvro first = (LightSensorAvro) a;
                    LightSensorAvro second = (LightSensorAvro) b;
                    return first.getLuminosity() == second.getLuminosity()
                           && first.getLinkQuality() == second.getLinkQuality();
                });

        comparators.put(ClimateSensorAvro.class,
                (a,  b) -> {
                    ClimateSensorAvro first = (ClimateSensorAvro) a;
                    ClimateSensorAvro second = (ClimateSensorAvro) b;
                    return first.getTemperatureC() == second.getTemperatureC()
                           && first.getHumidity() == second.getHumidity()
                           && first.getCo2Level() == second.getCo2Level();
                });

        comparators.put(TemperatureSensorAvro.class,
                (a,  b) -> {
                    TemperatureSensorAvro first = (TemperatureSensorAvro) a;
                    TemperatureSensorAvro second = (TemperatureSensorAvro) b;
                    return first.getTemperatureC() == second.getTemperatureC()
                            && first.getTemperatureF() == second.getTemperatureF();
                });

        return comparators;
    }
}
