package ru.yandex.practicum.exception;

public class NoCartFound extends RuntimeException {
    public NoCartFound(String message) {
        super(message);
    }
}
