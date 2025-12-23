package ru.yandex.practicum.dto.delivery;

import lombok.Builder;
import lombok.Getter;
import ru.yandex.practicum.dto.warehouse.AddressDto;

import java.util.UUID;

@Getter
@Builder
public class DeliveryDto {
    private UUID deliveryId;
    private AddressDto fromAddress;
    private AddressDto toAddress;
    private UUID orderId;
    private DeliveryState deliveryState;
}
