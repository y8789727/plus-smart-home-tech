package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {
    DeliveryDto createDelivery(DeliveryDto deliveryDto);

    void pickDelivery(UUID deliveryId);

    void deliverySuccess(UUID deliveryId);

    void deliveryFailed(UUID deliveryId);

    BigDecimal calcCostDelivery(OrderDto order);
}
