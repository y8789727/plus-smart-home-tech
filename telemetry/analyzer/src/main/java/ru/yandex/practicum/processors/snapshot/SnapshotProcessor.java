package ru.yandex.practicum.processors.snapshot;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.SnapshotService;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class SnapshotProcessor {

    private final SnapshotKafkaConfig kafkaConfig;
    private final SnapshotService snapshotService;

    public void start() {
        KafkaConsumer<String, SensorsSnapshotAvro> consumer = new KafkaConsumer<>(kafkaConfig.getProperties());

        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            consumer.subscribe(List.of(kafkaConfig.getTopic()));

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(kafkaConfig.getTimeout());

                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    log.info("Analyzer-snapshot: Обработка снэпшота {}", record.value());
                    snapshotService.processSnapshot(record.value());
                }
                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Analyzer.ERROR: Ошибка во время обработки снэпшота", e);
        } finally {
            try (consumer) {
                consumer.commitSync();
            }
        }
    }
}
