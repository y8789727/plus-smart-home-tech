package ru.yandex.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.hub.DeviceAction;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.hub.HubEventType;
import ru.yandex.practicum.dto.hub.ScenarioAddedEvent;
import ru.yandex.practicum.dto.hub.ScenarioCondition;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.util.Utils;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

@Component
public class ScenarioAddedHubEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedHubEventHandler(KafkaEventProducer eventProducer) {
        super(eventProducer);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public HubEventProto.PayloadCase getMessageTypeGrpc() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    private static DeviceActionAvro mapToAvro(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setValue(action.getValue())
                .setType(Utils.mapEnum(action.getType(), ActionTypeAvro.class))
                .build();
    }

    private static ScenarioConditionAvro mapToAvro(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setValue(condition.getValue())
                .setType(Utils.mapEnum(condition.getType(), ConditionTypeAvro.class))
                .setOperation(Utils.mapEnum(condition.getOperation(), ConditionOperationAvro.class))
                .build();
    }


    private static DeviceActionAvro mapToAvro(DeviceActionProto action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setValue(action.getValue())
                .setType(Utils.mapEnum(action.getType(), ActionTypeAvro.class))
                .build();
    }

    private static ScenarioConditionAvro mapToAvro(ScenarioConditionProto condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setValue(switch (condition.getType()) {
                            case MOTION, SWITCH ->  condition.getBoolValue();
                            default -> condition.getIntValue();
                        })
                .setType(Utils.mapEnum(condition.getType(), ConditionTypeAvro.class))
                .setOperation(Utils.mapEnum(condition.getOperation(), ConditionOperationAvro.class))
                .build();
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEvent event) {
        ScenarioAddedEvent specificEvent = (ScenarioAddedEvent) event;
        return ScenarioAddedEventAvro.newBuilder()
                .setName(specificEvent.getName())
                .setActions(specificEvent.getActions().stream().map(ScenarioAddedHubEventHandler::mapToAvro).toList())
                .setConditions(specificEvent.getConditions().stream().map(ScenarioAddedHubEventHandler::mapToAvro).toList())
                .build();
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEventProto event) {
        ScenarioAddedEventProto specificEvent = event.getScenarioAdded();
        return ScenarioAddedEventAvro.newBuilder()
                .setName(specificEvent.getName())
                .setActions(specificEvent.getActionList().stream().map(ScenarioAddedHubEventHandler::mapToAvro).toList())
                .setConditions(specificEvent.getConditionList().stream().map(ScenarioAddedHubEventHandler::mapToAvro).toList())
                .build();
    }
}
