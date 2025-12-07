package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class WarehouseExceptionHandler {
    @ExceptionHandler({NoSpecifiedProductInWarehouseException.class,
            SpecifiedProductAlreadyInWarehouseException.class,
            ProductInShoppingCartLowQuantityInWarehouse.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Exception handleBadRequest(final Exception e) {
        log.warn("404 {}", e.getMessage(), e);
        return new Exception(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Exception handleException(final Exception e) {
        log.warn("500 {}", e.getMessage(), e);
        return new Exception("На сервере произошла внутренняя ошибка", e);
    }
}
