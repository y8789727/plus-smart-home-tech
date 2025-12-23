package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.ProductReturnRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.OrderState;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDto createOrder(CreateNewOrderRequest newOrder, String userId);

    List<OrderDto> findUserOrders(String userName);

    OrderDto returnProducts(ProductReturnRequest returnRequest);

    OrderDto setOrderState(UUID orderId, OrderState state);

    OrderDto calculateTotal(UUID orderId);

    OrderDto calculateDelivery(UUID orderId);
}
