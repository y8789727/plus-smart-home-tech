package ru.yandex.practicum.dto.cart;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ShoppingCartDto {
    private String shoppingCartId;
    private Map<String, Integer> products;
}
