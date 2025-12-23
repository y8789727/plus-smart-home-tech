package ru.yandex.practicum.dto;

import ru.yandex.practicum.dal.model.Delivery;
import ru.yandex.practicum.dto.delivery.DeliveryDto;

public class DeliveryMapper {
    public static DeliveryDto mapToDto(Delivery delivery) {
        return DeliveryDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .orderId(delivery.getOrderId())
                .deliveryState(delivery.getState())
                .fromAddress(delivery.getFromAddress())
                .toAddress(delivery.getToAddress())
                .build();
    }
}
