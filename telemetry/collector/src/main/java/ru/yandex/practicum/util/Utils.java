package ru.yandex.practicum.util;

import java.time.Instant;
import com.google.protobuf.Timestamp;

public class Utils {
    public static <E extends Enum<E>, F extends Enum<F>> F mapEnum(E enum1, Class<F> enum2Class) {
        return Enum.valueOf(enum2Class, enum1.name());
    }

    public static Instant timestampToInstant(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}
