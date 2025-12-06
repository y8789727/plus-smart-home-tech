package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddProductToWarehouseRequest {
    private String productId;
    private Double quantity;
}
