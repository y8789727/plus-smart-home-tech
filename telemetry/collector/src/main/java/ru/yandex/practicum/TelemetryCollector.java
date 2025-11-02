package ru.yandex.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.yandex.practicum.producer.KafkaConfig;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(KafkaConfig.class)
public class TelemetryCollector {
    public static void main(String[] args) {
        log.info("Method launched (SpringApplication.run(TelemetryCollector.class, args))");
        SpringApplication.run(TelemetryCollector.class, args);
    }
}