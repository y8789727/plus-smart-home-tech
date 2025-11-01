package ru.practicum.producer;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.EnumMap;

@Component
public class KafkaEventProducer implements AutoCloseable {

    private final KafkaProducer<String, SpecificRecordBase> kafkaProducer;
    private final EnumMap<KafkaConfig.TopicType, String> topics;

    public KafkaEventProducer(KafkaConfig kafkaConfig) {
        this.kafkaProducer = new KafkaProducer<>(kafkaConfig.getProperties());

        this.topics = kafkaConfig.getTopics();
    }

    public void send(SpecificRecordBase event, String hubId, Instant timestamp, KafkaConfig.TopicType topicType) {
        String topic = topics.get(topicType);

        ProducerRecord<String, SpecificRecordBase> producerRecord = new ProducerRecord<>(topic, 1, timestamp.toEpochMilli(), hubId, event);

        kafkaProducer.send(producerRecord);
    }

    @Override
    public void close() throws Exception {
        kafkaProducer.close();
    }
}
