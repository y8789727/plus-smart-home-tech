package ru.yandex.practicum.dto.warehouse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DimensionDto {
    private Double width;
    private Double height;
    private Double depth;
}
