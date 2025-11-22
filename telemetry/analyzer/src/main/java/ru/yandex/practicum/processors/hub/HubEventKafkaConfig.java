package ru.yandex.practicum.processors.hub;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Properties;

@Getter
@Setter
@ConfigurationProperties("analyzer.hub-kafka-consumer")
public class HubEventKafkaConfig {
    private final Properties properties;
    private final String topic;
    private final Duration timeout;

    public HubEventKafkaConfig(Properties properties, String topic, Integer timeout) {
        this.properties = properties;
        this.topic = topic;
        this.timeout = Duration.ofMillis(timeout);
    }
}
