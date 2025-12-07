package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Getter;
import ru.yandex.practicum.dto.warehouse.DimensionDto;

import java.util.UUID;

@Getter
@Builder
public class NewProductInWarehouseRequest {
    private UUID productId;
    private Boolean fragile;
    private DimensionDto dimension;
    private Double weight;
}
