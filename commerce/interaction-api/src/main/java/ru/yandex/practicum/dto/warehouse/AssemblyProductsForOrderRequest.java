package ru.yandex.practicum.dto.warehouse;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
@Builder
public class AssemblyProductsForOrderRequest {
    private final Map<UUID, Integer> products;
    private final UUID orderId;
}
