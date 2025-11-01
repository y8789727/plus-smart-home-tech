package ru.practicum.producer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@ConfigurationProperties("collector.kafka")
public class KafkaConfig {
    private final Properties properties;
    private final EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);

    public KafkaConfig(Properties properties, Map<String, String> topics) {
        this.properties = properties;

        for (Map.Entry<String, String> topic : topics.entrySet()) {
            this.topics.put(TopicType.from(topic.getKey()), topic.getValue());
        }
    }

    public enum TopicType {
        SENSOR_EVENTS, HUB_EVENTS;

        public static TopicType from (String type) {
            for (TopicType topicType : values()) {
                if (topicType.name().equalsIgnoreCase(type.replace("-", "_"))) {
                    return topicType;
                }
            }
            return null;
        }
    }


}
