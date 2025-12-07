package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class AddProductToWarehouseRequest {
    private UUID productId;
    private Double quantity;
}
