package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import ru.yandex.practicum.dto.product.QuantityState;

import java.util.UUID;

@Getter
@Builder
public class SetProductQuantityStateRequest {
    @NotBlank(message = "Id продукта не может быть пустым")
    private UUID productId;
    @NotBlank(message = "Количество продукта не может быть пустым")
    private QuantityState quantityState;
}
