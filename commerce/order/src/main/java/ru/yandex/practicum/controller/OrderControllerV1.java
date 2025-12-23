package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.ProductReturnRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.OrderState;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderControllerV1 {
    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> findUserOrders(@RequestParam(name = "username") String userName) {
        validateUser(userName);
        return orderService.findUserOrders(userName);
    }

    @PutMapping
    public OrderDto createOrder(@RequestBody CreateNewOrderRequest newOrder,
                                @RequestHeader(name = "X-User-Id") String userId) {
        validateUser(userId);
        return orderService.createOrder(newOrder, userId);
    }

    @PostMapping("/return")
    public OrderDto returnProducts(@RequestBody ProductReturnRequest returnRequest) {
        return orderService.returnProducts(returnRequest);
    }

    @PostMapping("/payment")
    public OrderDto orderPaid(@RequestBody UUID orderId) {
        return orderService.setOrderState(orderId, OrderState.PAID);
    }

    @PostMapping("/payment/failed")
    public OrderDto orderPayFailed(@RequestBody UUID orderId) {
        return orderService.setOrderState(orderId, OrderState.PAYMENT_FAILED);
    }

    @PostMapping("/delivery")
    public OrderDto orderDelivered(@RequestBody UUID orderId) {
        return orderService.setOrderState(orderId, OrderState.DELIVERED);
    }

    @PostMapping("/delivery/failed")
    public OrderDto orderDeliveryFailed(@RequestBody UUID orderId) {
        return orderService.setOrderState(orderId, OrderState.DELIVERY_FAILED);
    }

    @PostMapping("/completed")
    public OrderDto orderCompleted(@RequestBody UUID orderId) {
        return orderService.setOrderState(orderId, OrderState.COMPLETED);
    }

    @PostMapping("/calculate/total")
    public OrderDto orderCalculateTotal(@RequestBody UUID orderId) {
        return orderService.calculateTotal(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto orderCalculateDelivery(@RequestBody UUID orderId) {
        return orderService.calculateDelivery(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto orderAssembled(@RequestBody UUID orderId) {
        return orderService.setOrderState(orderId, OrderState.ASSEMBLED);
    }

    @PostMapping("/assembly/failed")
    public OrderDto orderAssemblyFailed(@RequestBody UUID orderId) {
        return orderService.setOrderState(orderId, OrderState.ASSEMBLY_FAILED);
    }

    private void validateUser(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new NotAuthorizedUserException("Пользователь не определен!");
        }
    }
}
