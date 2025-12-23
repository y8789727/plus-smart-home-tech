package ru.yandex.practicum.dto.warehouse;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ShippedToDeliveryRequest {
    private final UUID orderId;
    private final UUID deliveryId;
}
