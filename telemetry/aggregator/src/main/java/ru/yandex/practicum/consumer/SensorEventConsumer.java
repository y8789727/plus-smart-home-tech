package ru.yandex.practicum.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.producer.KafkaSnapshotProducer;
import ru.yandex.practicum.storage.SensorSnapshotStorage;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorEventConsumer {

    private final SensorSnapshotStorage sensorSnapshotStorage;
    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final KafkaSnapshotProducer producer;

    public void start() {

        KafkaConsumer<String, SensorEventAvro> consumer = new KafkaConsumer<>(kafkaConsumerConfig.getProperties());

        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            consumer.subscribe(List.of(kafkaConsumerConfig.getTopic()));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(kafkaConsumerConfig.getTimeout());

                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    handleRecord(record);
                }

                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    private void handleRecord(ConsumerRecord<String, SensorEventAvro> record) throws InterruptedException {
        Optional<SensorsSnapshotAvro> snapshotOpt = sensorSnapshotStorage.updateState(record.value());
        snapshotOpt.ifPresent(sensorsSnapshotAvro -> producer.send(sensorsSnapshotAvro, sensorsSnapshotAvro.getHubId(), Instant.now()));
        producer.flush();
    }

}