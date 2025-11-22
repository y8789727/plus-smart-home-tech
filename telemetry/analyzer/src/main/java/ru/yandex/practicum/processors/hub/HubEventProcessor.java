package ru.yandex.practicum.processors.hub;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.service.HubEventService;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {

    private final HubEventKafkaConfig kafkaConfig;
    private final Map<Class<?>, Consumer<HubEventAvro>> eventProcessors;

    public HubEventProcessor(HubEventKafkaConfig kafkaConfig, HubEventService hubEventService) {
        this.kafkaConfig = kafkaConfig;
        this.eventProcessors = Map.of(
                DeviceAddedEventAvro.class, hubEventService::addHubDevice,
                DeviceRemovedEventAvro.class, hubEventService::removeHubDevice,
                ScenarioAddedEventAvro.class,hubEventService::addScenario,
                ScenarioRemovedEventAvro.class, hubEventService::removeScenario);
    }

    @Override
    public void run() {
        KafkaConsumer<String, HubEventAvro> consumer = new KafkaConsumer<>(kafkaConfig.getProperties());

        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            consumer.subscribe(List.of(kafkaConfig.getTopic()));

            while (true) {

                ConsumerRecords<String, HubEventAvro> records = consumer.poll(kafkaConfig.getTimeout());

                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    handleRecord(record);
                }

                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Analyzer.ERROR: Ошибка во время обработки событий от хаба", e);
        } finally {
            try (consumer) {
                consumer.commitSync();
            }
        }
    }

    private void handleRecord(ConsumerRecord<String, HubEventAvro> record) {
        eventProcessors.get(record.value().getPayload().getClass()).accept(record.value());
    }
}
