package ru.yandex.practicum.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDto {
    private String productId;
    @NotBlank(message = "Наименование продукта обязательно")
    private String productName;
    @NotBlank(message = "Описание продукта обязательно")
    private String description;
    private String imageSrc;
    @NotNull(message = "Количество продукта обязательно")
    private QuantityState quantityState;
    @NotNull(message = "Статус продукта обязателен")
    private ProductState productState;
    private ProductCategory productCategory;
    @NotNull(message = "Цена продукта обязательна")
    private BigDecimal price;
}
