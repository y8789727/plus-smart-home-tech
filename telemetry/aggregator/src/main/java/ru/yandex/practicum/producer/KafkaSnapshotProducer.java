package ru.yandex.practicum.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;

@Component
public class KafkaSnapshotProducer implements AutoCloseable {

    private final KafkaProducer<String, SensorsSnapshotAvro> kafkaProducer;
    private final String topic;

    public KafkaSnapshotProducer(KafkaProducerConfig producerConfig) {
        this.kafkaProducer = new KafkaProducer<>(producerConfig.getProperties());

        this.topic = producerConfig.getTopic();
    }

    public void send(SensorsSnapshotAvro snapshot, String hubId, Instant timestamp) {
        ProducerRecord<String, SensorsSnapshotAvro> producerRecord = new ProducerRecord<>(topic, null, timestamp.toEpochMilli(), hubId, snapshot);

        kafkaProducer.send(producerRecord);
    }

    public void flush() {
        kafkaProducer.flush();
    }

    @Override
    public void close() {
        kafkaProducer.close();
    }
}
