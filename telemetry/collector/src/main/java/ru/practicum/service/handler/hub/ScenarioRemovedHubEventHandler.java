package ru.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.practicum.dto.hub.HubEvent;
import ru.practicum.dto.hub.HubEventType;
import ru.practicum.dto.hub.ScenarioRemovedEvent;
import ru.practicum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedHubEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {


    public ScenarioRemovedHubEventHandler(KafkaEventProducer eventProducer) {
        super(eventProducer);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEvent event) {
        ScenarioRemovedEvent specificEvent = (ScenarioRemovedEvent) event;
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(specificEvent.getName())
                .build();
    }
}
