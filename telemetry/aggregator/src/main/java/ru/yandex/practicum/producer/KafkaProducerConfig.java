package ru.yandex.practicum.producer;

import java.util.Properties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("aggregator.kafka-producer")
public class KafkaProducerConfig {
    private final Properties properties;
    private final String topic;

    public KafkaProducerConfig(Properties properties, String topic) {
        this.properties = properties;
        this.topic = topic;
    }
}
