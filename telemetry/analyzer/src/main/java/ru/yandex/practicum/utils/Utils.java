package ru.yandex.practicum.utils;

public class Utils {
    public static <E extends Enum<E>, F extends Enum<F>> F mapEnum(E enum1, Class<F> enum2Class) {
        return Enum.valueOf(enum2Class, enum1.name());
    }
}