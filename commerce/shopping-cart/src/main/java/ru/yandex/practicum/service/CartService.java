package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartService {

    ShoppingCartDto getShoppingCartByUser(String userName);

    ShoppingCartDto addProductsToCart(String userName, Map<UUID, Integer> products);

    void deactivateUserCart(String userName);

    ShoppingCartDto removeProductsFromCart(String userName, List<UUID> products);

    ShoppingCartDto changeProductQuantity(String userName, ChangeProductQuantityRequest productQuantity);
}
