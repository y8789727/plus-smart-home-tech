package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.consumer.SensorEventConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorRunner implements CommandLineRunner {

    private final SensorEventConsumer sensorEventConsumer;

    @Override
    public void run(String... args) throws Exception {
        sensorEventConsumer.start();
    }
}
