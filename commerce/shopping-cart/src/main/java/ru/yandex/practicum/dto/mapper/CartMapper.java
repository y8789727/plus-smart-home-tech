package ru.yandex.practicum.dto.mapper;

import ru.yandex.practicum.dal.model.Cart;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;

import java.util.Map;
import java.util.stream.Collectors;

public class CartMapper {
    public static ShoppingCartDto mapToDto(Cart cart) {
        return ShoppingCartDto.builder()
                .shoppingCartId(cart.getId())
                .products(cart.getProducts().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getQuantity())))
                .build();
    }
}
