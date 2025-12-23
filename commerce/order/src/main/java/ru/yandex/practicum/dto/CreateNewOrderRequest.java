package ru.yandex.practicum.dto;

import lombok.Builder;
import lombok.Getter;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;

@Getter
@Builder
public class CreateNewOrderRequest {
    private final ShoppingCartDto shoppingCart;
    private final AddressDto deliveryAddress;
}
