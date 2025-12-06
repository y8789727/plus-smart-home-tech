package ru.yandex.practicum.exception;

public class NoProductInWarehouse extends RuntimeException {
    public NoProductInWarehouse(String message) {
        super(message);
    }
}
