package ru.yandex.practicum.processors.snapshot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Properties;

@Getter
@Setter
@ConfigurationProperties("analyzer.snapshot-kafka-consumer")
public class SnapshotKafkaConfig {
    private final Properties properties;
    private final String topic;
    private final Duration timeout;

    public SnapshotKafkaConfig(Properties properties, String topic, Integer timeout) {
        this.properties = properties;
        this.topic = topic;
        this.timeout = Duration.ofMillis(timeout);
    }
}
