package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeProductQuantityRequest {
    private String productId;
    private Integer newQuantity;
}
