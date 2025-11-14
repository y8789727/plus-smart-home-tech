package ru.yandex.practicum.consumer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Getter
@Setter
@ConfigurationProperties("aggregator.kafka-consumer")
public class KafkaConsumerConfig {
    private final Properties properties;
    private final List<String> topics;
    private final Duration timeout;

    public KafkaConsumerConfig(Properties properties, String topic, Integer timeout) {
        this.properties = properties;
        this.topics = List.of(topic);
        this.timeout = Duration.ofMillis(timeout);
    }
}
