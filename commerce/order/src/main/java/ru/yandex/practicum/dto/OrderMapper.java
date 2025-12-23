package ru.yandex.practicum.dto;

import ru.yandex.practicum.dal.model.Order;
import ru.yandex.practicum.dto.order.OrderDto;

import java.util.Map;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDto mapToDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .shoppingCartId(order.getCartId())
                .products(order.getProducts().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getQuantity())))
                .paymentId(order.getPaymentId())
                .deliveryId(order.getDeliveryId())
                .state(order.getState())
                .deliveryWeight(order.getProductsWeight())
                .deliveryVolume(order.getProductsVolume())
                .fragile(order.getFragile())
                .totalPrice(order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .productPrice(order.getProductsPrice())
                .build();
    }
}
