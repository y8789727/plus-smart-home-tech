package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.model.Action;
import ru.yandex.practicum.dal.model.ActionType;
import ru.yandex.practicum.dal.model.Condition;
import ru.yandex.practicum.dal.model.ConditionOperation;
import ru.yandex.practicum.dal.model.ConditionType;
import ru.yandex.practicum.dal.model.Scenario;
import ru.yandex.practicum.dal.model.Sensor;
import ru.yandex.practicum.dal.model.SensorType;
import ru.yandex.practicum.dal.repository.ScenarioRepository;
import ru.yandex.practicum.dal.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.utils.Utils;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HubEventServiceImpl implements HubEventService {
    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;

    @Override
    public void addHubDevice(HubEventAvro event) {
        DeviceAddedEventAvro payload = (DeviceAddedEventAvro) event.getPayload();

        Optional<Sensor> sensorOpt = sensorRepository.findByIdAndHubId(payload.getId(), event.getHubId());

        if (sensorOpt.isEmpty()) {
            sensorRepository.save(Sensor.builder()
                    .id(payload.getId())
                    .hubId(event.getHubId())
                    .type(Utils.mapEnum(payload.getType(), SensorType.class))
                    .build());
        }
    }

    @Override
    public void removeHubDevice(HubEventAvro event) {
        DeviceRemovedEventAvro payload = (DeviceRemovedEventAvro) event.getPayload();

        Optional<Sensor> sensorOpt = sensorRepository.findByIdAndHubId(payload.getId(), event.getHubId());

        sensorOpt.ifPresent(sensorRepository::delete);
    }

    @Override
    public void addScenario(HubEventAvro event) {
        ScenarioAddedEventAvro payload = (ScenarioAddedEventAvro) event.getPayload();

        Optional<Scenario> scenarioOpt = scenarioRepository.findByHubIdAndName(event.getHubId(), payload.getName());

        scenarioOpt.ifPresent(scenarioRepository::delete);

        Map<String, Condition> conditions = payload.getConditions().stream()
                .collect(Collectors.toMap(ScenarioConditionAvro::getSensorId, c -> {
                    Integer value = null;
                    if (c.getValue() != null) {
                        if (c.getValue() instanceof Boolean) {
                            value = (Boolean) c.getValue() ? 1 : 0;
                        } else {
                            value = (Integer) c.getValue();
                        }
                    }

                    return Condition.builder()
                            .type(Utils.mapEnum(c.getType(), ConditionType.class))
                            .operation(Utils.mapEnum(c.getOperation(), ConditionOperation.class))
                            .value(value)
                            .build();
                }));

        Map<String, Action> actions = payload.getActions().stream()
                .collect(Collectors.toMap(DeviceActionAvro::getSensorId, a ->
                        Action.builder()
                            .type(Utils.mapEnum(a.getType(), ActionType.class))
                            .value(a.getValue() != null ? a.getValue() : null)
                            .build()
                ));

        Scenario scenario = Scenario.builder()
                .hubId(event.getHubId())
                .name(payload.getName())
                .conditions(conditions)
                .actions(actions)
                .build();

        scenarioRepository.save(scenario);
    }

    @Override
    public void removeScenario(HubEventAvro event) {
        ScenarioRemovedEventAvro payload = (ScenarioRemovedEventAvro) event.getPayload();

        Optional<Scenario> scenarioOpt = scenarioRepository.findByHubIdAndName(event.getHubId(), payload.getName());

        scenarioOpt.ifPresent(scenarioRepository::delete);
    }
}
