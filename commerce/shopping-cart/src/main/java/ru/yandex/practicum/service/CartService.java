package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;

public interface CartService {

    ShoppingCartDto getShoppingCartByUser(String userName);

    ShoppingCartDto addProductsToCart(String userName, Map<String, Integer> products);

    void deactivateUserCart(String userName);

    ShoppingCartDto removeProductsFromCart(String userName, List<String> products);

    ShoppingCartDto changeProductQuantity(String userName, ChangeProductQuantityRequest productQuantity);
}
