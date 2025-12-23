package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
@Builder
public class ProductReturnRequest {
    private UUID orderId;
    private Map<UUID, Integer> products;
}
