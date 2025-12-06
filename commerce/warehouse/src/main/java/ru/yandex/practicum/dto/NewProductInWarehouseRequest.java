package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.dto.warehouse.DimensionDto;

@Data
@Builder
public class NewProductInWarehouseRequest {
    private String productId;
    private Boolean fragile;
    private DimensionDto dimension;
    private Double weight;
}
