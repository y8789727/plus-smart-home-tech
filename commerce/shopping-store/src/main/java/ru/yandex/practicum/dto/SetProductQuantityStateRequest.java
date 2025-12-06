package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.dto.product.QuantityState;

@Data
@Builder
public class SetProductQuantityStateRequest {
    @NotBlank(message = "Id продукта не может быть пустым")
    private String productId;
    @NotBlank(message = "Количество продукта не может быть пустым")
    private QuantityState quantityState;
}
